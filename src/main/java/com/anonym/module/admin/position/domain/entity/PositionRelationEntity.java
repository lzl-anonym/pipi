package com.anonym.module.admin.position.domain.entity;

import com.anonym.common.domain.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * 岗位关联关系
 */
@Data
@TableName("t_position_relation")
public class PositionRelationEntity extends BaseEntity {

    /**
     * 岗位ID
     */
    private Long positionId;

    /**
     * 员工ID
     */
    private Long employeeId;

}
