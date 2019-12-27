package com.anonym.module.zteuser;

import com.anonym.common.constant.ResponseCodeConst;

/**
 * 用户状态码常量类
 * 15101 - 15200
 */
public class ZteUserResponseCodeConst extends ResponseCodeConst {

    /**
     * 用户不存在
     */
    public static final ZteUserResponseCodeConst USER_NOT_EXISTS = new ZteUserResponseCodeConst(15101, "此中兴用户不存在！");


    public ZteUserResponseCodeConst(int code, String msg) {
        super(code, msg);
    }
}
