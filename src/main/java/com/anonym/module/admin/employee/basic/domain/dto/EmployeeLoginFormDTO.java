package com.anonym.module.admin.employee.basic.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 登录
 */
@Data
public class EmployeeLoginFormDTO {

    @NotNull(message = "登录名不能为空")
    @ApiModelProperty(example = "sa")
    private String loginName;

    @NotNull(message = "密码不能为空")
    @ApiModelProperty(example = "123456")
    private String loginPwd;

//    @NotNull(message = "验证码id不能为空")
//    @ApiModelProperty(value = "验证码uuid")
//    private String codeUuid;
//
//    @NotBlank(message = "验证码不能为空")
//    @ApiModelProperty(value = "验证码")
//    private String code;

}
