package com.anonym.module.admin.employee.login.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class LoginPrivilegeDTO {

    @ApiModelProperty("权限key")
    private String key;

    //@ApiModelPropertyEnum(enumDesc = "菜单类型", value = PrivilegeTypeEnum.class)
    @ApiModelProperty("子权限")
    private Integer type;

    @ApiModelProperty("子权限")
    private List<LoginPrivilegeDTO> children;
}
