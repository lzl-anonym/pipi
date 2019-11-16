package com.anonym.module.fuseuser.record.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author lizongliang
 * @date 2019-11-16 16:23
 */
@Data
public class FaceFuseRecordVO {


    @ApiModelProperty("用户id")
    private Integer faceFuseUserId;

    @ApiModelProperty("参与时间")
    private Date createTime;

    @ApiModelProperty("手机")
    private String phone;
}
