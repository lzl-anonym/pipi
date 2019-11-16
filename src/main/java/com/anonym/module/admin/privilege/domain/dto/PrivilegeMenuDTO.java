package com.anonym.module.admin.privilege.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class PrivilegeMenuDTO {

    //@ApiModelPropertyEnum(enumDesc = "菜单类型", value = PrivilegeTypeEnum.class)
    @NotNull(message = "type不能为空")
    private Integer type;

    @ApiModelProperty("菜单名")
    @NotNull(message = "菜单名不能为空")
    private String menuName;

    @ApiModelProperty("菜单Key")
    @NotNull(message = "菜单Key不能为空")
    private String menuKey;

    @ApiModelProperty("父级菜单Key,根节点不传")
    private String parentKey;

    @ApiModelProperty("排序字段")
    @NotNull(message = "菜单项顺序不能为空")
    private Integer sort;
}
