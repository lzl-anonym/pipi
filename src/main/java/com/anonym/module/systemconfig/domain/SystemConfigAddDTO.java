package com.anonym.module.systemconfig.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class SystemConfigAddDTO {

    @ApiModelProperty("参数key")
    @NotBlank(message = "参数key不能为空")
    private String configKey;

    @ApiModelProperty("参数的值")
    @NotBlank(message = "参数的值不能为空")
    private String configValue;

    @ApiModelProperty("参数名称")
    @NotBlank(message = "参数名称不能为空")
    private String configName;

    @ApiModelProperty("参数类别")
    @NotBlank(message = "参数类别不能为空")
    private String configGroup;

    @ApiModelProperty("备注")
    private String remark;
}
