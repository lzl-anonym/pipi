package com.anonym.module.admin.role.roleemployee.domain;

import com.anonym.common.domain.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * [ 角色 员工关系]
 */
@Data
@TableName("t_role_employee")
public class RoleEmployeeEntity extends BaseEntity {

    private Long roleId;

    private Long employeeId;
}
