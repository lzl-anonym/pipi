package com.anonym.module.admin.employee.basic.domain.dto;

import com.anonym.module.admin.position.domain.dto.PositionRelationResultDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 员工列表DTO
 */
@Data
public class EmployeeDTO {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("登录账号")
    private String loginName;

    @ApiModelProperty("别名")
    private String nickName;

    @ApiModelProperty("员工名称")
    private String actualName;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("身份证")
    private String idCard;

    @ApiModelProperty("出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    @ApiModelProperty("创建者id")
    private Long createUser;

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty("是否离职")
    private Integer isLeave;

    @ApiModelProperty("是否被禁用")
    private Integer isDisabled;

    @ApiModelProperty("是否删除")
    private Integer isDelete;

    @ApiModelProperty("部门名称")
    private String departmentName;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("岗位关联信息")
    private List<PositionRelationResultDTO> positionRelationList;

    @ApiModelProperty("岗位名称")
    private String positionName;

    @ApiModelProperty("超管 true:是 false:非")
    private Boolean isSuper;

}