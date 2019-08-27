package com.anonym.config;

import com.anonym.interceptor.PrivilegeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    @Autowired
    private PrivilegeInterceptor privilegeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("================Loading  拦截器=================");
        registry.addInterceptor(privilegeInterceptor).addPathPatterns("/**");
    }

}
