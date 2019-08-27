package com.anonym.module.roleprivilege.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 权限树
 */
@Data
public class RolePrivilegeTreeDTO {

    @ApiModelProperty("权限ID")
    private Integer roleId;

    @ApiModelProperty("权限列表")
    private List<RolePrivilegeSimpleDTO> privilege;

    @ApiModelProperty("选中的权限")
    private List<Integer> selectedIds;
}
