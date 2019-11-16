package com.anonym.module.user;

import com.anonym.common.constant.ResponseCodeConst;

/**
 * 用户状态码常量类
 * 14000 - 14500
 */
public class UserResponseCodeConst extends ResponseCodeConst {

    /**
     * 用户不存在
     */
    public static final UserResponseCodeConst USER_NOT_EXISTS = new UserResponseCodeConst(14000, "用户不存在！");

    /**
     * 帐号不存在
     */
    public static final UserResponseCodeConst ACCOUNT_NOT_EXISTS = new UserResponseCodeConst(14001, "帐号不存在！");

    /**
     * 密码错误
     */
    public static final UserResponseCodeConst PWD_ERROR = new UserResponseCodeConst(14002, "密码错误！");

    /**
     * 帐号已禁用
     */
    public static final UserResponseCodeConst ACCOUNT_DISABLED = new UserResponseCodeConst(14003, "帐号异常，请联系客服！");

    public static final UserResponseCodeConst LOGIN_ERROR = new UserResponseCodeConst(14004, "您还未登录或登录失效，请重新登录！");

    /**
     * 帐号已存在
     */
    public static final UserResponseCodeConst ACCOUNT_EXISTS = new UserResponseCodeConst(14005, "帐号已存在！");

    /**
     * 您的账号状态异常
     */
    public static final UserResponseCodeConst STATUS_ERROR = new UserResponseCodeConst(14006, "您的账号状态异常");

    /**
     * 您还未绑定手机号
     */
    public static final UserResponseCodeConst NOT_BIND_PHONR = new UserResponseCodeConst(14007, "您还未绑定手机号");

    /**
     * 回馈金可用余额不足
     */
    public static final UserResponseCodeConst AVAILABLE_MONEY_ERROR = new UserResponseCodeConst(14008, "回馈金可用余额不足！");

    /**
     * 回馈金参数异常
     */
    public static final UserResponseCodeConst REWARD_PARAMETER_ERROR = new UserResponseCodeConst(14009, "回馈金参数异常！");

    /**
     * 手机号已存在
     */
    public static final UserResponseCodeConst PHONE_EXIST = new UserResponseCodeConst(14010, "手机号已存在！");

    /**
     * 手机号绑定失败
     */
    public static final UserResponseCodeConst BIND_PHONE_ERROR = new UserResponseCodeConst(14011, "手机号绑定失败！");

    public UserResponseCodeConst(int code, String msg) {
        super(code, msg);
    }
}
