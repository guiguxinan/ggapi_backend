package com.gg.springbootinit.model.enums;

/**
 * 接口信息状态枚举
 */
public enum InterfaceInfoStatusEnum {
    ONLINE("上线",0),
    OFFLINE("下线",1);

    private String text;
    private int value;

    InterfaceInfoStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }
}
