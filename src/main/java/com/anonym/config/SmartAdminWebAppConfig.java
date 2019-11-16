package com.anonym.config;

import com.anonym.constant.CommonConst;
import com.anonym.interceptor.AdminAuthorityInterceptor;
import com.anonym.interceptor.AppAuthorityInterceptor;
import com.anonym.interceptor.CommonAuthorityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class SmartAdminWebAppConfig implements WebMvcConfigurer {

    @Autowired
    private AdminAuthorityInterceptor adminAuthorityInterceptor;

    @Autowired
    private AppAuthorityInterceptor appAuthorityInterceptor;

    @Autowired
    private CommonAuthorityInterceptor commonAuthorityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAuthorityInterceptor).addPathPatterns(CommonConst.API_PREFIX_ADMIN + "/**");
        registry.addInterceptor(commonAuthorityInterceptor).addPathPatterns(CommonConst.API_PREFIX_COMMON + "/**");
        registry.addInterceptor(appAuthorityInterceptor).addPathPatterns(CommonConst.API_PREFIX_APP + "/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/druidMonitor").setViewName("redirect:druid/index.html");
        registry.addViewController("/swaggerApi").setViewName("redirect:swagger-ui.html");
    }
}
