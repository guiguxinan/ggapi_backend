package com.gg.springbootinit.model.dto.userInterfaceInfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 创建请求
 *
 * @author <a href="https://github.com/guiguxinan">程序员鬼鬼</a>

 */
@Data
public class UserInterfaceInfoAddRequest implements Serializable {
    /**
     * 调用用户 id
     */
    private Long userId;
    /**
     * 接口 id
     */
    private Long interfaceInfoId;
    /**
     * 总调用次数
     */
    private Integer totalNum;
    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 0-正常，1-禁用
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}