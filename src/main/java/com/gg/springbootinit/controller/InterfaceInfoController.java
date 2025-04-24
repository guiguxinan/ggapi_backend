package com.gg.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.ggapiclientsdk.client.GgApiClient;
import com.gg.springbootinit.annotation.AuthCheck;
import com.gg.springbootinit.common.*;
import com.gg.springbootinit.constant.UserConstant;
import com.gg.springbootinit.exception.BusinessException;
import com.gg.springbootinit.exception.ThrowUtils;
import com.gg.springbootinit.model.dto.interfaceInfo.InterfaceInfoAddRequest;
import com.gg.springbootinit.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.gg.springbootinit.model.dto.interfaceInfo.InterfaceInfoUpdateRequest;
import com.gg.springbootinit.model.entity.InterfaceInfo;
import com.gg.springbootinit.model.entity.User;
import com.gg.springbootinit.model.enums.InterfaceInfoStatusEnum;
import com.gg.springbootinit.service.InterfaceInfoService;
import com.gg.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 接口管理
 *
 * @author <a href="https://github.com/guiguxinan">程序员鬼鬼</a>

 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private GgApiClient ggApiClient;

    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
//        List<String> tags = interfaceInfoAddRequest.getTags();
//        if (tags != null) {
//            interfaceInfo.setTags(JSONUtil.toJsonStr(tags));
//        }
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
//        interfaceInfo.setFavourNum(0);
//        interfaceInfo.setThumbNum(0);
        boolean result = interfaceInfoService.save(interfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param interfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }
    /**
     * 发布（仅管理员）
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        // 1.校验接口是否存在
        // 如果id为null或者id小于0，抛出业务异常，表示请求参数错误
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断该接口是否存在
        // 获取idRequest对象的id属性
        Long id = idRequest.getId();
        // 根据id查询接口信息数据
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        // 如果查询结果为空，则抛出异常
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);

        // 2. 判断该接口是否可以调用
        // 模拟假数据。创建一个user
        com.gg.ggapiclientsdk.model.User user = new com.gg.ggapiclientsdk.model.User();
        user.setName("test");
        // 调用ggapiClient的getUsernameByPost方法传入user对象，赋值给userName变量
        String userName = ggApiClient.getUserNameByPost(user);
        // 如果userName为空或空白字符串
        if (StringUtils.isBlank(userName)) {
            // 抛出系统错误异常，表示系统内部异常，并附带错误信息“接口验证失败”
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口验证失败");
        }
        // 3. 修改接口数据库中的状态字段为上线
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        // 调用interfaceInfoService中的updateById方法，将返回结果赋值给result变量
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        // 返回一个成功的响应，响应体携带result值
        return ResultUtils.success(result);
    }
    /**
     * 下线（仅管理员）
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        // 校验接口是否存在
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断该接口是否存在
        // 获取idRequest对象的id属性
        Long id = idRequest.getId();
        // 根据id查询接口信息数据
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        // 如果查询结果为空，则抛出异常
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);

        // 3. 修改接口数据库中的状态字段为下线
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        // 调用interfaceInfoService中的updateById方法，将返回结果赋值给result变量
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        // 返回一个成功的响应，响应体携带result值
        return ResultUtils.success(result);
    }
    /**
     * 分页获取列表（仅管理员）
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<InterfaceInfo>> listPostByPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
        return ResultUtils.success(interfaceInfoPage);
    }

    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(Long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        // 如果查询结果为空，则抛出异常
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);

        return ResultUtils.success(interfaceInfo);
    }
}
