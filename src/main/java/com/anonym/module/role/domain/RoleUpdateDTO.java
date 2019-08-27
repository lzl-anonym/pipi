package com.anonym.module.role.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 角色更新修改DTO
 *
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class RoleUpdateDTO extends RoleAddDTO {

    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    @NotNull(message = "角色id不能为空")
    protected Integer id;

}
