package com.gg.springbootinit.model.dto.interfaceInfo;

import lombok.Data;
import java.io.Serializable;

/**
 * 更新请求
 *
 * @author <a href="https://github.com/guiguxinan">程序员鬼鬼</a>

 */
@Data
public class InterfaceInfoUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 接口状态 (0-关闭，1-开启)
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;


    private static final long serialVersionUID = 1L;
}