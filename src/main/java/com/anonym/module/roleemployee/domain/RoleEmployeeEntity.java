package com.anonym.module.roleemployee.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * [ 角色 员工关系]
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("t_role_employee")
public class RoleEmployeeEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer roleId;

    private Integer employeeId;


    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;
}
