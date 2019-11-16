package com.anonym.module.fuseuser.domain;

import com.anonym.utils.SmartVerificationUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author lizongliang
 * @date 2019-11-16 15:09
 */
@Data
public class FaceFuseUserAddDTO {

    @ApiModelProperty("手机号")
    @Length(max = 11, message = "最多不超过11字符")
    @NotBlank(message = "负责人手机号不能为空")
    @Pattern(regexp = SmartVerificationUtil.PHONE_REGEXP, message = "手机号格式错误")
    private String phone;
}
