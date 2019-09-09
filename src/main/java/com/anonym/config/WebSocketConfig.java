package com.anonym.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 开启WebSocket支持
 *
 * @author lizongliang
 * @date 2019-09-09 9:56
 */
//@Configuration
public class WebSocketConfig {
    // TODO: 2019-09-09   取消websocket配置

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        System.out.println("=====================开启WebSocket支持====================");
        return new ServerEndpointExporter();
    }

}

