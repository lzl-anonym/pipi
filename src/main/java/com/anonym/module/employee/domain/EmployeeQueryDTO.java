package com.anonym.module.employee.domain;

import com.anonym.common.domain.PageBaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 员工列表DTO
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class EmployeeQueryDTO extends PageBaseDTO {

    @ApiModelProperty("姓名")
    private String keyword;

    @ApiModelProperty("部门id")
    private Integer departmentId;

    @ApiModelProperty("是否被禁用 0否1是")
    private Integer isDisabled;

    @ApiModelProperty(value = "删除状态 0否 1是 不需要传", hidden = true)
    private Integer isDelete;
}
