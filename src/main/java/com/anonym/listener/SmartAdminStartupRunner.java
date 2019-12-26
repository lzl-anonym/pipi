package com.anonym.listener;

import com.anonym.common.constant.ResponseCodeConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmartAdminStartupRunner implements CommandLineRunner {


    @Override
    public void run(String... args) {

        log.info("###################### Init start ######################");

        ResponseCodeConst.init();


        log.info("###################### Init complete ######################");
    }
}