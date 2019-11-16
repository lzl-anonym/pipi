package com.anonym.module.fuseuser.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

/**
 * @author lizongliang
 * @date 2019-11-16 14:44
 */
@Data
@TableName("t_face_fuse_user")
public class FaceFuseUserEntity {

    @TableId(type = IdType.AUTO)
    private Integer faceFuseUserId;

    /**
     * 手机
     */
    private String phone;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 密码
     */
    private String password;

    private Date updateTime;

    private Date createTime;
}
