package com.anonym.module.user.basic.domain;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户 DTO 类
 */
@Data
public class UserDTO {

    private Long userId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 帐号
     */
    private String phone;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 是否禁用
     */
    private Boolean disabledFlag;

    private String remark;

    /**
     * 最后一次登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 最后一次登录 ip
     */
    private String lastLoginIp;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;

    private String token;

}
