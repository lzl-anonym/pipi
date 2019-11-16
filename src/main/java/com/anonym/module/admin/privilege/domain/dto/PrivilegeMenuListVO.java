package com.anonym.module.admin.privilege.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class PrivilegeMenuListVO {

    @ApiModelProperty("菜单名")
    private String menuName;

    @ApiModelProperty("菜单Key")
    private String menuKey;

    @ApiModelProperty("菜单父级Key")
    private String menuParentKey;

    @ApiModelProperty("子菜单列表")
    private List<PrivilegeMenuListVO> menuList;

}
