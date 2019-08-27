package com.anonym.module.department.domain;

import com.anonym.module.employee.domain.EmployeeDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DepartmentDTO {

    @ApiModelProperty("部门id")
    private Integer id;

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty("部门简称")
    private String shortName;

    @ApiModelProperty("部门负责人姓名")
    private String managerName;

    @ApiModelProperty("部门负责人id")
    private Integer managerId;

    @ApiModelProperty("子部门")
    @JsonProperty("children")
    private List<DepartmentDTO> childrenDepartment;

    @ApiModelProperty("父级部门id")
    private Integer parentId;

    @ApiModelProperty("同级上一个元素id")
    private Integer preId;

    @ApiModelProperty("同级下一个元素id")
    private Integer nextId;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("父级部门名称")
    private String parentName;

    @ApiModelProperty("部门员工列表")
    private List<EmployeeDTO> employees;

    @ApiModelProperty("大区")
    private String area;

    @ApiModelProperty("地区删除状态")
    private Integer deleted;

    @ApiModelProperty("上次更新时间")
    private Date updateTime;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
