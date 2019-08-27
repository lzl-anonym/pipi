package com.anonym.module.privilege.constant;

import com.anonym.common.domain.BaseEnum;

import java.util.Arrays;
import java.util.Optional;

/**
 * 权限划分 1管理端权限 2web端权限
 *
 * @author cyj
 * @date 2017-12-29 下午 1:51
 */
public enum PrivilegeScopeEnum implements BaseEnum {

    /**
     * 管理端权限
     */
    BACK(1),

    /**
     * web端权限
     */
    WEB(2);

    private Integer value;

    PrivilegeScopeEnum(Integer value) {
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

    public static PrivilegeScopeEnum selectByValue(Integer value) {
        Optional<PrivilegeScopeEnum> first = Arrays.stream(PrivilegeScopeEnum.values()).filter(e -> e.getValue().equals(value)).findFirst();
        return !first.isPresent() ? null : first.get();
    }
}
