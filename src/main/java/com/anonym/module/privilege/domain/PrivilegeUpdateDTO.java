package com.anonym.module.privilege.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;


@EqualsAndHashCode(callSuper = false)
@Data
public class PrivilegeUpdateDTO extends PrivilegeAddDTO {

    @ApiModelProperty("id")
    @NotNull(message = "id不能为空")
    private Integer id;

}
