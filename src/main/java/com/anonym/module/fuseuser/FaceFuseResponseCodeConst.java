package com.anonym.module.fuseuser;

import com.anonym.common.constant.ResponseCodeConst;

public class FaceFuseResponseCodeConst extends ResponseCodeConst {

    /**
     * 帐号已存在
     */
    public static final FaceFuseResponseCodeConst PHONE_EXIST = new FaceFuseResponseCodeConst(15000, "帐号已存在！");


    /**
     * 获取token失败！
     */
    public static final FaceFuseResponseCodeConst TOKEN_ERROR = new FaceFuseResponseCodeConst(15001, "醉了，获取token失败！");
    /**
     * 合成失败！
     */
    public static final FaceFuseResponseCodeConst FUSE_ERROR = new FaceFuseResponseCodeConst(15002, "搜嘎！运气不好，合成失败！");


    public FaceFuseResponseCodeConst(int code, String msg) {
        super(code, msg);
    }
}
