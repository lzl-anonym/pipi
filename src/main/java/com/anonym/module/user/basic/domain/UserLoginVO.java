package com.anonym.module.user.basic.domain;

import com.anonym.common.anno.ApiModelPropertyEnum;
import com.anonym.common.constant.GenderEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户登录 展示信息 VO 类
 */
@Data
public class UserLoginVO {

    @ApiModelProperty("id")
    private Long userId;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelPropertyEnum(GenderEnum.class)
    private Integer gender;

    @ApiModelProperty("最后一次登录时间")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty("最后一次登录ip")
    private String lastLoginIp;

    @ApiModelProperty("token")
    private String token;
}

