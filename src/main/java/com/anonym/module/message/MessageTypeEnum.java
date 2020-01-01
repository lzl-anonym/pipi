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
     * 1 兴光舞台
     */
    MESSAGE(1, "兴光舞台"),

    /**
     * 2 兴声留言机
     */
    STANDBY(2, "兴声留言机");

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
