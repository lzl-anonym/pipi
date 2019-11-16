package com.anonym.module.admin.notice.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class NoticeUpdateDTO extends NoticeAddDTO {

    @ApiModelProperty("id")
    private Long id;
}
