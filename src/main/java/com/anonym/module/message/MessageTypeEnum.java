package com.anonym.module.message;

import com.anonym.common.domain.BaseEnum;


/**
 * 留言类型枚举
 *
 * @author lizongliang
 * @date 2019-12-20 18:48
 */
public enum MessageTypeEnum implements BaseEnum {

    /**
     * 1 我要留言
     */
    MESSAGE(1, "我要留言"),

    /**
     * 2 备用
     */
    STANDBY(2, "备用");

    private Integer value;

    private String desc;

    MessageTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 获取枚举类的值
     *
     * @return Integer
     */
    @Override
    public Integer getValue() {
        return value;
    }

    /**
     * 获取枚举类的说明
     *
     * @return String
     */
    @Override
    public String getDesc() {
        return desc;
    }
}
