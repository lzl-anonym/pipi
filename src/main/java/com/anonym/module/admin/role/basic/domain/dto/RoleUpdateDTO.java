package com.anonym.module.admin.role.basic.domain.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RoleUpdateDTO extends RoleAddDTO {

    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    @NotNull(message = "角色id不能为空")
    protected Long id;

}
