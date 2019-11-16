package com.anonym.module.admin.notice.domain.dto;

import com.anonym.common.anno.ApiModelPropertyEnum;
import com.anonym.common.constant.JudgeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
public class NoticeReceiveDTO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("消息标题")
    private String title;

    @ApiModelProperty("消息创建人")
    private Long createUser;

    @ApiModelProperty("消息创建人名称")
    private String createUserName;

    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date receiveTime;

    @ApiModelPropertyEnum(enumDesc = "读取状态", value = JudgeEnum.class)
    private Integer readStatus;
}
