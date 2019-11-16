package com.anonym.module.admin.notice.domain.dto;

import com.anonym.common.domain.PageBaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class NoticeQueryDTO extends PageBaseDTO {

    @ApiModelProperty("开始日期")
    private String startDate;

    @ApiModelProperty("结束日期")
    private String endDate;

    @ApiModelProperty("消息标题")
    private String title;

    @ApiModelProperty(value = "是否删除", hidden = true)
    private Integer deleted;

}
