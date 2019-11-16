package com.anonym.module.admin.role.roleprivilege.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 角色功能权限
 */
@Data
public class RolePrivilegeSimpleDTO {

    private String parentKey;

    /**
     * 功能名称
     */
    private String name;

    private Integer type;

    private String key;

    private String url;

    /**
     * 子模块
     */
    private List<RolePrivilegeSimpleDTO> children;

}
