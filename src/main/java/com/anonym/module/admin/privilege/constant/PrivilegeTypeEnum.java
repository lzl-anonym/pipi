package com.anonym.module.admin.privilege.constant;

import com.anonym.common.domain.BaseEnum;

import java.util.Arrays;
import java.util.Optional;


public enum PrivilegeTypeEnum implements BaseEnum {

    /**
     * 菜单模块
     */
    MENU(1),

    /**
     * 功能点
     */
    POINTS(2);

    private Integer value;

    PrivilegeTypeEnum(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getDesc() {
        return null;
    }

    public static PrivilegeTypeEnum selectByValue(Integer value) {
        Optional<PrivilegeTypeEnum> first = Arrays.stream(PrivilegeTypeEnum.values()).filter(e -> e.getValue().equals(value)).findFirst();
        return !first.isPresent() ? null : first.get();
    }
}
