package com.anonym.module.log.orderoperatelog.constant;

import com.anonym.common.constant.ResponseCodeConst;


public class OrderOperateLogOperateTypeConst extends ResponseCodeConst {

    public static final OrderOperateLogOperateTypeConst ADD = new OrderOperateLogOperateTypeConst(8001, "创建并提交");

    public static final OrderOperateLogOperateTypeConst UPDATE = new OrderOperateLogOperateTypeConst(8002, "修改并提交");

    public static final OrderOperateLogOperateTypeConst DELETE = new OrderOperateLogOperateTypeConst(8003, "删除");

    public static final OrderOperateLogOperateTypeConst SHELF = new OrderOperateLogOperateTypeConst(8004, "修改为上架");

    public static final OrderOperateLogOperateTypeConst OBTAINED = new OrderOperateLogOperateTypeConst(8005, "修改为下架");

    private OrderOperateLogOperateTypeConst(int code, String msg) {
        super(code, msg);
    }

}
