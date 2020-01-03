package com.anonym.common.constant;

import com.anonym.common.domain.BaseEnum;


public enum CommentSortTypeEnum implements BaseEnum {


    ASC(1, "ASC"),

    DESC(2, "DESC");

    private Integer value;

    private String desc;


    public static final String INFO = "排序类型：1正序 | 2倒序";

    CommentSortTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }


    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
