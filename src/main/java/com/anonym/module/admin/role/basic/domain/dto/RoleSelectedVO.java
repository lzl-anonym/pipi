package com.anonym.module.admin.role.basic.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class RoleSelectedVO extends RoleVO {

    @ApiModelProperty("角色名称")
    private Boolean selected;
}
