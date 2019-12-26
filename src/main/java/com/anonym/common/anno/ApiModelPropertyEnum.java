package com.anonym.common.anno;

import com.anonym.common.domain.BaseEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiModelPropertyEnum {

    /**
     * 枚举类对象
     *
     * @return
     */
    Class<? extends BaseEnum> value();

    String example() default "";

    boolean required() default true;

    String dataType() default "";

    String enumDesc() default "";

}
