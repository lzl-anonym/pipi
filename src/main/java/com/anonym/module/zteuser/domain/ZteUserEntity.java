package com.anonym.module.zteuser.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

/**
 * @author lizongliang
 * @date 2019-12-27 10:47
 */
@Data
@TableName("t_zte_user")
public class ZteUserEntity {


    /**
     * 中兴用户id
     */
    @TableId(type = IdType.INPUT)
    private Long zteUserId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 删除状态  1删除  0不删除
     */
    private Boolean deleteFlag;

    private Date updateTime;

    private Date createTime;


}
