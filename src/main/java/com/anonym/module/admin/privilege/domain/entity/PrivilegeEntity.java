package com.anonym.module.admin.privilege.domain.entity;

import com.anonym.common.domain.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("t_privilege")
public class PrivilegeEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3848408566432915214L;

    /**
     * 功能权限类型：1.模块 2.页面 3.功能点 4.子模块
     */
    private Integer type;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 路由name 英文关键字
     */
    private String key;

    private String url;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 父级key
     */
    private String parentKey;

}
