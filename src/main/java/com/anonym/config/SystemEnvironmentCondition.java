package com.anonym.config;

import com.anonym.constant.SystemEnvironmentEnum;
import com.anonym.utils.SmartBaseEnumUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 是否是正式环境
 */
@Configuration
public class SystemEnvironmentCondition implements Condition {

    @Value("${spring.profiles.active}")
    private String systemEnvironment;

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return !SystemEnvironmentEnum.PROD.equalsValue(systemEnvironment);
    }

    @Bean
    public SystemEnvironmentEnum initEnvironment() {
        return SmartBaseEnumUtil.getEnumByValue(systemEnvironment, SystemEnvironmentEnum.class);
    }
}
