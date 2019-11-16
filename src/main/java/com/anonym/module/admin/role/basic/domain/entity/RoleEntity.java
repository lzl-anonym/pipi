package com.anonym.module.admin.role.basic.domain.entity;

import com.anonym.common.domain.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;


@Data
@TableName("t_role")
public class RoleEntity extends BaseEntity {

    private String roleName;

    private String remark;
}
