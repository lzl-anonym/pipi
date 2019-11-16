package com.anonym;

import com.anonym.module.websocket.WebSocketAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author lizongliang
 */
@EnableScheduling
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
public class AnonymApplication {

    public static void main(String[] args) {
//        SpringApplication.run(AnonymApplication.class, args);
        ConfigurableApplicationContext applicationContext = SpringApplication.run(AnonymApplication.class, args);
        // 设置 WebSocketAdminServer 里的 applicationContext
        WebSocketAdminServer.setApplicationContext(applicationContext);
    }

}
