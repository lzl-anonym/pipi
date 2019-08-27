package com.anonym.module.role.domain;

import com.anonym.common.domain.PageBaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = false)
@Data
public class RoleQueryDTO extends PageBaseDTO {

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色id")
    private String roleId;
}
