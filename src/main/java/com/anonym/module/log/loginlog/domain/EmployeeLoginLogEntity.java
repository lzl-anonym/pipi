package com.anonym.module.log.loginlog.domain;

import com.anonym.common.domain.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Builder;
import lombok.Data;

/**
 * [ 用户登录日志]
 */
@Data
@Builder
@TableName("t_employee_login_log")
public class EmployeeLoginLogEntity extends BaseEntity {

    /**
     * 员工id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

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

}
