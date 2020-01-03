package com.anonym.common.validator.bigdecimal;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BigDecimalValidator.class)// 自定义验证的处理类
public @interface CheckBigDecimal {


    String value();


    ComparisonSymbolEnum symbolEnum();


    String message() default "非法的数值";


    boolean required() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
