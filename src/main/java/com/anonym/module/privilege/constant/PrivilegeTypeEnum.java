package com.anonym.module.privilege.constant;

import com.anonym.common.domain.BaseEnum;

import java.util.Arrays;
import java.util.Optional;

/**
 * 功能权限类型：1.模块 2.页面 3.功能点
 *
 * @author cyj
 * @date 2017-12-29 下午 1:51
 */
public enum PrivilegeTypeEnum implements BaseEnum {

    /**
     * 模块
     */
    MODULE(1),

    /**
     * 子模块
     */
    CHILDREN_MODULE(2),

    /**
     * 页面
     */
    PAGE(3),

    /**
     * 功能点
     */
    POINTS(4);

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
