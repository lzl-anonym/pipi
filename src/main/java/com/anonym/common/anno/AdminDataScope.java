package com.anonym.common.anno;

import com.anonym.module.admin.datascope.constant.DataScopeTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * [ 标记后管用户数据范围的注解 ]

 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AdminDataScope {

    DataScopeTypeEnum dataScopeType() default DataScopeTypeEnum.DEFAULT;

    /**
     * 第几个where 条件 从0开始
     *
     * @return
     */
    int whereIndex() default 0;

    String joinSql() default "";

}
