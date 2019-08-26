package com.anonym.module.employee.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * 登录dto
 *
 * @author Administrator
 */
@Data
public class EmployeeLoginDTO {

	@NotNull(message = "登录名不能为空")
	@ApiModelProperty(example = "sa")
	private String loginName;

	@NotNull(message = "密码不能为空")
	@ApiModelProperty(example = "123456")
	private String loginPwd;
	// TODO: 2019-08-26   暂时先不要验证码
	//    @NotNull(message = "验证码id不能为空")
	@ApiModelProperty(value = "验证码uuid")
	private String codeUuid;

	//    @NotNull(message = "验证码不能为空")
	@ApiModelProperty(value = "验证码")
	private String code;

}
