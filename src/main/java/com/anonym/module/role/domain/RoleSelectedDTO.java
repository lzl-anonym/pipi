package com.anonym.module.role.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class RoleSelectedDTO extends RoleDTO {

    @ApiModelProperty("角色名称")
    private Boolean selected;
}
