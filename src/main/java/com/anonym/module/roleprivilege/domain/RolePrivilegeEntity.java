package com.anonym.module.roleprivilege.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * [ 角色 权限关系 ]
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("t_role_privilege")
public class RolePrivilegeEntity {


    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 角色 id
     */
    private Integer roleId;

    /**
     * 功能权限 id
     */
    private Integer privilegeId;


    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

}
