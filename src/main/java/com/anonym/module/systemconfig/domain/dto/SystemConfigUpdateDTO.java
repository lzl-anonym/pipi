package com.anonym.module.systemconfig.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class SystemConfigUpdateDTO extends SystemConfigAddDTO {

    @ApiModelProperty("id")
    @NotNull(message = "id不能为空")
    private Long id;
}
