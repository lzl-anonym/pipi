package com.anonym.module.userloginlog.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * [ 用户登录日志]
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("t_user_login_log")
public class UserLoginLogEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 员工id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户类型 0后管 1前台
     */
    private Integer userType;

    /**
     * 用户ip
     */
    private String remoteIp;

    /**
     * 用户端口
     */
    private Integer remotePort;

    /**
     * 所在地址
     */
    private String remoteAddress;

    /**
     * 浏览器
     */
    private String remoteBrowser;

    /**
     * 操作系统
     */
    private String remoteOs;

    /**
     * 登录状态
     */
    private Integer loginStatus;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;
}
