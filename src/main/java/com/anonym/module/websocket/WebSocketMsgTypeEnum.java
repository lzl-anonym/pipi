package com.anonym.module.websocket;

import com.anonym.common.domain.BaseEnum;


public enum WebSocketMsgTypeEnum implements BaseEnum {

    SYS_NOTICE(1, "系统通知"),

    SITE_MSG(2, "站内信"),

    HEART_BEAT(3, "心跳");

    private Integer value;

    private String desc;

    WebSocketMsgTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
