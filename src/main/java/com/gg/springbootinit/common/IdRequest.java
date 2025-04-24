package com.gg.springbootinit.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口发布下线请求
 *
 * @author <a href="https://github.com/guiguxinan">程序员鬼鬼</a>

 */
@Data
public class IdRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}