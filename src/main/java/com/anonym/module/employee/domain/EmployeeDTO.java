package com.anonym.module.employee.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 员工列表DTO
 *
 * @author Administrator
 */
@Data
public class EmployeeDTO {

    @ApiModelProperty("主键id")
    private Integer id;

    @ApiModelProperty("登录账号")
    private String loginName;

    @ApiModelProperty("姓名")
    private String actualName;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("身份证")
    private String idCard;

    @ApiModelProperty("出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    @ApiModelProperty("创建者id")
    private Integer createUser;

    @ApiModelProperty("部门id")
    private Integer departmentId;

    @ApiModelProperty("是否离职")
    private Integer isLeave;

    @ApiModelProperty("是否被禁用")
    private Integer isDisabled;

    @ApiModelProperty("部门名称")
    private String departmentName;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
