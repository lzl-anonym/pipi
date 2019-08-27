package com.anonym.module.roleprivilege.domain;

import lombok.Data;

import java.util.List;

/**
 * 角色功能权限
 *
 */
@Data
public class RolePrivilegeSimpleDTO {

    /**
     * 主键id
     */
    private Integer id;

    private Integer parentId;

    /**
     * 功能名称
     */
    private String name;

    private Integer type;

    /**
     * 图标
     */
    private String icon;

    private String routerKey;

    private String url;

    /**
     * 子模块
     */
    private List<RolePrivilegeSimpleDTO> children;

}
