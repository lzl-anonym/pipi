package com.anonym.module.roleprivilege.domain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class RolePrivilegeDTO {

    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    @NotNull(message = "角色id不能为空")
    private Integer roleId;

    /**
     * 功能权限id 集合
     */
    @ApiModelProperty("功能权限id集合")
    @NotNull(message = "功能权限集合不能为空")
    private List<Integer> privilegeIdList;

}
