package com.anonym.module.admin.notice.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class NoticeReceiveQueryDTO extends NoticeQueryDTO {

    @ApiModelProperty(value = "当前登录人", hidden = true)
    private Long employeeId;

    @ApiModelProperty(value = "发送状态", hidden = true)
    private Integer sendStatus;

}
