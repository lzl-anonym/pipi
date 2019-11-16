package com.anonym.module.user.login.domain;

import com.anonym.common.anno.ApiModelPropertyEnum;
import com.anonym.common.constant.GenderEnum;
import com.anonym.common.validator.en.CheckEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录 DTO 类
 */
@Data
public class UserLoginDTO {

    @ApiModelProperty("微信code")
    @NotBlank(message = "微信code不能为空")
    private String code;

    @ApiModelProperty("微信头像url")
    @NotBlank(message = "微信头像url不能为空")
    @Length(max = 200, message = "微信头像url最多100位")
    private String avatar;

    @ApiModelPropertyEnum(value = GenderEnum.class, example = "1")
    @CheckEnum(enumClazz = GenderEnum.class, message = "性别类型错误")
    private Integer gender;

    @ApiModelProperty("微信昵称")
    @NotBlank(message = "微信昵称不能为空")
    @Length(max = 20, message = "微信昵称最多20位")
    private String nickname;

    @ApiModelProperty(value = "最后一次登录 ip", hidden = true)
    private String lastLoginIp;

}
