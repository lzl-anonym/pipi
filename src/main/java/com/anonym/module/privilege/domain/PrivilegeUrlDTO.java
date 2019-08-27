package com.anonym.module.privilege.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class PrivilegeUrlDTO {

    @ApiModelProperty("注释说明")
    private String comment;

    @ApiModelProperty("controller.method")
    private String name;

    @ApiModelProperty("url")
    private String url;
}
