package com.anonym.listener;

import com.anonym.common.constant.ResponseCodeConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 应用启动加载
 */
@Slf4j
@Component
public class SmartAdminStartupRunner implements CommandLineRunner {


    @Override
    public void run(String... args) {

        log.info("###################### init start ######################");

        // 初始化状态码
        ResponseCodeConst.init();

        // 初始化微信全局AccessToken
        //weChatBaseService.initGlobalAccessToken();

        log.info("###################### init complete ######################");
    }
}