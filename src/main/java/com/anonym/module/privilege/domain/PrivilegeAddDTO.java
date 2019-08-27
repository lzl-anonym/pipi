package com.anonym.module.privilege.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PrivilegeAddDTO {

    @ApiModelProperty("功能权限类型")
    @NotNull(message = "功能权限类型不能为空")
    private Integer type;

    @ApiModelProperty("菜单名称")
    @NotBlank(message = "菜单名称不能为空")
    private String name;

    @ApiModelProperty("功能Key")
    @NotBlank(message = "功能Key不能为空")
    private String routerKey;

    @ApiModelProperty("页面地址")
    private String url;

    @ApiModelProperty("菜单/子菜单对应页面路径")
    private String page;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("是否显示")
    @NotNull(message = "是否显示不能为空")
    private Integer isShow;

    @ApiModelProperty("父级id")
    private Integer parentId;

    @ApiModelProperty("排序")
    @NotNull(message = "排序不能为空")
    private Integer seq;

    @ApiModelProperty("是否有效")
    @NotNull(message = "是否有效不能为空 是否有效 1.有效")
    private Integer isEnable;

    @ApiModelProperty("权限划分 1管理端权限 2web端权限")
    @NotNull(message = "权限划分不能为空")
    private Integer scope;

}
