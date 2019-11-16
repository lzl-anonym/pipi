package com.anonym.module.admin.position.domain.entity;

import com.anonym.common.domain.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * 岗位
 */
@Data
@TableName("t_position")
public class PositionEntity extends BaseEntity {

    /**
     * 岗位名称
     */
    private String positionName;

    /**
     * 岗位描述
     */
    private String remark;

}
