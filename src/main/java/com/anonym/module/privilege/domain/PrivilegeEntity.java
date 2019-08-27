package com.anonym.module.privilege.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;


@EqualsAndHashCode(callSuper = false)
@Data
@TableName("t_privilege")
public class PrivilegeEntity implements Serializable {

    private static final long serialVersionUID = 3848408566432915214L;


    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

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
    private String routerKey;

    /**
     * 路由path/type=3为API接口
     */
    private String url;

    /**
     * 菜单/子菜单对应页面路径
     */
    private String page;

    /**
     * 图标
     */
    private String icon;

    /**
     * 是否显示：1是
     */
    private Integer isShow;

    /**
     * 父级id
     */
    private Integer parentId;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 是否有效 1.有效
     */
    private Integer isEnable;

    /**
     * 1管理端权限 2web端权限
     */
    private Integer scope;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;
}
