package com.gg.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gg.springbootinit.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.gg.springbootinit.model.dto.post.PostQueryRequest;
import com.gg.springbootinit.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gg.springbootinit.model.entity.Post;

/**
* @author gg
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2025-03-06 17:07:54
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean b);

    QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest);
}
