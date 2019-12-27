package com.anonym.module.zteuser.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ZteUserDTO {

    @ApiModelProperty("中兴用户id")
    private Long zteUserId;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("删除状态  true删除  false不删除  ")
    private Boolean deleteFlag;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    private String token;

}
