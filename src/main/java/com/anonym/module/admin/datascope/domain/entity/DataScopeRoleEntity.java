package com.anonym.module.admin.datascope.domain.entity;

import com.anonym.common.domain.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("t_role_data_scope")
public class DataScopeRoleEntity extends BaseEntity {

    /**
     * 数据范围id
     */
    private Integer dataScopeType;

    /**
     * 数据范围类型
     */
    private Integer viewType;

    /**
     * 角色id
     */
    private Long roleId;
}
