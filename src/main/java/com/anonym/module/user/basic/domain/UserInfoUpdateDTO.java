package com.anonym.module.user.basic.domain;

import com.anonym.utils.SmartVerificationUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author lizongliang
 * @date 2019-10-23 10:00
 */
@Data
public class UserInfoUpdateDTO {

    @ApiModelProperty(name = "手机号", example = "18837903681")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = SmartVerificationUtil.PHONE_REGEXP, message = "手机号格式错误")
    private String phone;

    @ApiModelProperty(name = "短信验证码")
    @NotBlank(message = "短信验证码不能为空")
    @Length(max = 6, message = "短信验证码最多6个字符")
    private String verificationCode;
}
