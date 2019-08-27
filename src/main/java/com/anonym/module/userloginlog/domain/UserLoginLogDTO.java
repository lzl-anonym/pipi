package com.anonym.module.userloginlog.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * [ 用户登录日志 ]
 */
@Data
public class UserLoginLogDTO {

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("员工id")
    private Integer userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("用户类型 0后管 1前台")
    private Integer userType;

    @ApiModelProperty("用户ip")
    private String remoteIp;

    @ApiModelProperty("用户端口")
    private Integer remotePort;

    @ApiModelProperty("所在地址")
    private String remoteAddress;

    @ApiModelProperty("浏览器")
    private String remoteBrowser;

    @ApiModelProperty("操作系统")
    private String remoteOs;

    @ApiModelProperty("登录状态")
    private Integer loginStatus;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
