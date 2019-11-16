package com.anonym.module.admin.role.roleprivilege.domain.entity;

import com.anonym.common.domain.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * [ 角色 权限关系 ]
 */
@Data
@TableName("t_role_privilege")
public class RolePrivilegeEntity extends BaseEntity {

    /**
     * 角色 id
     */
    private Long roleId;

    /**
     * 功能权限 id
     */
    private String privilegeKey;

}
