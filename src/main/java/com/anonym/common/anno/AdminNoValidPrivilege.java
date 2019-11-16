package com.anonym.common.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * [ 标记后管用户不需要权限验证的注解 ]

 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminNoValidPrivilege {

}
