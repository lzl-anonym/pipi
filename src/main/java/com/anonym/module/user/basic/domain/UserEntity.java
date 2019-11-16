package com.anonym.module.user.basic.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户表 实体类
 */
@Data
@TableName("t_user")
public class UserEntity {

    @TableId(type = IdType.AUTO)
    private Long userId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 微信 open id
     */
    private String weChatOpenId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 密码
     */
    private String password;

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

    /**
     * 用户类型 true:商家 false:普通用户
     */
    private Boolean userFlag;

    /**
     * 微信 session key
     */
    private String weChatSessionKey;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;

}
