package com.anonym.module.log.orderoperatelog.domain.entity;

import com.anonym.common.domain.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 各种单据操作记录
 *
 * </p>
 *
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_order_operate_log")
public class OrderOperateLogEntity extends BaseEntity {

    /**
     * 各种单据的id
     */
    private Long orderId;

    /**
     * 单据类型
     */
    private Integer orderType;

    /**
     * 操作类型
     */
    private Integer operateType;

    /**
     * 操作类型 对应的中文
     */
    private String operateContent;

    /**
     * 操作备注
     */
    private String operateRemark;

    /**
     * 员工id
     */
    private Long employeeId;

    /**
     * 员工名称
     */
    private String employeeName;

    /**
     * 额外信息
     */
    private String extData;

}
