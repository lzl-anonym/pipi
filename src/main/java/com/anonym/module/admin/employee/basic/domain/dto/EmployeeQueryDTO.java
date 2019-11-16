package com.anonym.module.admin.employee.basic.domain.dto;

import com.anonym.common.domain.PageBaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 员工列表DTO
 */
@Data
public class EmployeeQueryDTO extends PageBaseDTO {

    private String phone;

    private String actualName;

    private String keyword;

    private Long departmentId;

    private Integer isLeave;

    private Integer isDisabled;

    /**
     * 删除状态 0否 1是
     */
    @ApiModelProperty("删除状态 0否 1是 不需要传")
    private Integer isDelete;

    @ApiModelProperty("员工id集合")
    private List<Long> employeeIds;

}
