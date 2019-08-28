package com.anonym.module.serviceitem;

import com.anonym.common.constant.ResponseCodeConst;

/**
 * 角色业务状态码 6001 - 6999
 */
public class ServiceItemResponseCodeConst extends ResponseCodeConst {

    /**
     * 10501 角色名称已存在
     */
    public static final ServiceItemResponseCodeConst ITEM_NAME_EXISTS = new ServiceItemResponseCodeConst(1600, "服务项名称已存在！");


    public ServiceItemResponseCodeConst(int code, String msg) {
        super(code, msg);
    }
}
