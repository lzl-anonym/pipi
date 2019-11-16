package com.anonym.module.admin.employee.basic.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 修改密码所需参数
 */
@Data
public class EmployeeUpdatePwdDTO {

    @ApiModelProperty("新密码")
    @NotNull
    private String pwd;

    @ApiModelProperty("原密码")
    @NotNull
    private String oldPwd;

}
