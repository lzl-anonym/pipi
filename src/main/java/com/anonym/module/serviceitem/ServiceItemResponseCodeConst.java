package com.anonym.module.serviceitem;

import com.anonym.common.constant.ResponseCodeConst;

/**
 * 角色业务状态码 6001 - 6999
 */
public class ServiceItemResponseCodeConst extends ResponseCodeConst {


    /**
     * 服务项名称已存在！
     */
    public static final ServiceItemResponseCodeConst ITEM_NAME_EXISTS = new ServiceItemResponseCodeConst(1600, "服务项名称已存在！");

    /**
     * 该会员已经有这个服务项目了，请按照姓名查找出来直接编辑就可以了，不需要新增会员！
     */
    public static final ServiceItemResponseCodeConst MEMBER_SERVICE_ITEM_EXISTS = new ServiceItemResponseCodeConst(1601, "该会员已经有这个服务项目了，请按照姓名查找出来直接编辑就可以了，不需要新增会员！");

    /**
     * 会员名已经存在了，请确认一下吧！
     */
    public static final ServiceItemResponseCodeConst MEMBER_NAME_EXISTS = new ServiceItemResponseCodeConst(1602, "会员名已经存在了，请确认一下吧！");

    /**
     * 该服务项不存在！
     */
    public static final ServiceItemResponseCodeConst ITEM_NAME_NOT_EXISTS = new ServiceItemResponseCodeConst(1603, "该服务项不存在！");

    /**
     * 该会员不存在！
     */
    public static final ServiceItemResponseCodeConst MEMBER_NOT_EXISTS = new ServiceItemResponseCodeConst(1604, "该会员不存在！");

    /**
     * 次数扣取失败！该会员已经没有可消费的次数了，请收取现金或者提醒用户充值！
     */
    public static final ServiceItemResponseCodeConst OVER = new ServiceItemResponseCodeConst(1605, "次数扣取失败！该会员已经没有可消费的次数了，请收取现金或者提醒用户充值！");

    /**
     * 消费成功！该用户次数已经使用完毕，请提醒用户！
     */
    public static final ServiceItemResponseCodeConst OVER_ZERO = new ServiceItemResponseCodeConst(1606, "消费成功！该用户次数已经使用完毕，请提醒用户！");


    public ServiceItemResponseCodeConst(int code, String msg) {
        super(code, msg);
    }
}
