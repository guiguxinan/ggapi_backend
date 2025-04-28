package com.gg.springbootinit.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口调用请求
 *
 * @author <a href="https://github.com/guiguxinan">程序员鬼鬼</a>

 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户请求参数
     */
    private String userRequestParams;

    private static final long serialVersionUID = 1L;
}