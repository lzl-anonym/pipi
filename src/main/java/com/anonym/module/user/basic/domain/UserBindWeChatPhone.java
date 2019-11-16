package com.anonym.module.user.basic.domain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户绑定微信手机号
 */
@Data
public class UserBindWeChatPhone {

    @ApiModelProperty(hidden = true)
    private Long userId;

    @ApiModelProperty("加密数据")
    @NotBlank(message = "加密数据不能为空")
    private String encryptedData;

    @ApiModelProperty("iv")
    @NotBlank(message = "iv不能为空")
    private String iv;
}
