package com.anonym.module.systemconfig.constant;

import com.anonym.common.constant.ResponseCodeConst;


public class SystemConfigResponseCodeConst extends ResponseCodeConst {

    /**
     * 配置参数已存在 10201
     */
    public static final SystemConfigResponseCodeConst ALREADY_EXIST = new SystemConfigResponseCodeConst(1500, "配置参数已存在");

    /**
     * 配置参数不存在 10203
     */
    public static final SystemConfigResponseCodeConst NOT_EXIST = new SystemConfigResponseCodeConst(1501, "配置参数不存在");

    public SystemConfigResponseCodeConst(int code, String msg) {
        super(code, msg);
    }
}
