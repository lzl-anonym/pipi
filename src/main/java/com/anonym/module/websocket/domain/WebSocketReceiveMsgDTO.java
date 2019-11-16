package com.anonym.module.websocket.domain;

import lombok.Data;

/**
 * 接收消息 DTO 类
 */
@Data
public class WebSocketReceiveMsgDTO {

    /**
     * 消息类型
     */
    private Integer messageType;

    /**
     * 具体消息内容
     */
    private String jsonStr;

}
