package com.anonym.module.department.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 部门实体类
 * t_department 数据表
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName(value = "t_department")
public class DepartmentEntity implements Serializable {

    private static final long serialVersionUID = -6787726615141147044L;


    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 部门简称
     */
    private String shortName;

    /**
     * 负责人员工 id
     */
    private Integer managerId;

    /**
     * 部门父级id
     */
    private Integer parentId;

    /**
     * 删除状态
     */
    private Integer deleted;


    /**
     * 排序
     */
    private Integer sort;

    /**
     * 大区
     */
    private String area;


    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

}
