package com.anonym.module.role.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class RoleDTO {

    @ApiModelProperty("角色ID")
    private Integer id;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色备注")
    private String remark;
}
