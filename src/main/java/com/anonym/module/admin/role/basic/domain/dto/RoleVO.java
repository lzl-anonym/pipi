package com.anonym.module.admin.role.basic.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class RoleVO {

    @ApiModelProperty("角色ID")
    private Long id;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色备注")
    private String remark;
}
