package com.anonym.module.log.orderoperatelog.constant;

import com.anonym.common.domain.BaseEnum;


public enum OrderOperateLogOrderTypeEnum implements BaseEnum {

    GOODS(1, "商品"),

    ROUTE(2, "路线"),

    ACTIVITY(3, "活动"),

    COUPON(4, "优惠券"),

    SHOP(5, "店铺"),

    SHOP_STATEMENT(6, "商家结算单"),

    RED_ENVELOPE(7, "红包");


    private int type;

    private String typeName;

    public static final String INFO = "";

    OrderOperateLogOrderTypeEnum(int type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    public int getType() {
        return type;
    }

    /**
     * 获取枚举类的值
     *
     * @return Integer
     */
    @Override
    public Integer getValue() {
        return type;
    }

    /**
     * 获取枚举类的说明
     *
     * @return String
     */
    @Override
    public String getDesc() {
        return typeName;
    }
}
