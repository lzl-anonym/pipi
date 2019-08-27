package com.anonym.common.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 不需要权限验证
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NoValidPrivilege {

}
