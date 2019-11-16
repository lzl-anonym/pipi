package com.anonym.module.user.basic.domain;

import com.anonym.common.anno.ApiModelPropertyEnum;
import com.anonym.common.anno.FileKey;
import com.anonym.common.constant.GenderEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户展示 VO 类
 */
@Data
public class UserAdminVO {

    @ApiModelProperty("id")
    private Long userId;

    @ApiModelProperty("微信openid")
    private String weChatOpenId;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("头像")
    @FileKey
    private String avatar;

    @ApiModelProperty("手机")
    private String phone;

    @ApiModelPropertyEnum(GenderEnum.class)
    private Integer gender;

    @ApiModelProperty("禁用状态")
    private Boolean disabledFlag;

    @ApiModelProperty("上次登录时间")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty("注册时间")
    private LocalDateTime createTime;

}
