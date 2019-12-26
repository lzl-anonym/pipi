package com.anonym.common.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AdminAuthorityLevel {

    int level() default LOW;

    /**
     * <li>LOW:低，不需要用户登录
     */
    int LOW = 0;

    int MIDDLE = 1;

    int HEIGHT = 2;

}
