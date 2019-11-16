package com.anonym.module.systemconfig.domain.dto;

import com.anonym.common.domain.PageBaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class SystemConfigQueryDTO extends PageBaseDTO {

    @ApiModelProperty("参数KEY")
    private String key;

    @ApiModelProperty("参数类别")
    private String configGroup;

}
