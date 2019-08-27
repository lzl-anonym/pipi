package com.anonym.common.token;

import com.anonym.common.constant.ResponseCodeConst;

/**
 * 员工常量类
 * 1201-1400
 *
 * @author lidoudou
 * @date 2017年12月19日下午19:04:52
 */
public class TokenCommonResponseCodeConst extends ResponseCodeConst {

    public static final TokenCommonResponseCodeConst LOGIN_ERROR = new TokenCommonResponseCodeConst(1000, "您还未登录或登录失效，请重新登录！");

    public static final TokenCommonResponseCodeConst NOT_HAVE_PRIVILEGES = new TokenCommonResponseCodeConst(1001, "对不起，您没有权限哦！");

    public TokenCommonResponseCodeConst(int code, String msg) {
        super(code, msg);
    }
}
