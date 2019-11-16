package com.anonym.module.websocket.domain;

import com.anonym.module.websocket.WebSocketMsgTypeEnum;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class WebSocketSendMsgDTO {

    /**
     * 消息类型
     */
    private WebSocketMsgTypeEnum msgTypeEnum;

    /**
     * 消息体
     */
    private String content;

    /**
     * 发送者
     */
    private Long fromUserId;

    /**
     * 接收者，系统通知可为null
     */
    private Long toUserId;

}
