package com.anonym.module.admin.employee.basic.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class EmployeeUpdateRolesDTO {

    @ApiModelProperty("员工id")
    @NotNull(message = "员工id不能为空")
    private Long employeeId;

    @ApiModelProperty("角色ids")
    private List<Long> roleIds;

}
