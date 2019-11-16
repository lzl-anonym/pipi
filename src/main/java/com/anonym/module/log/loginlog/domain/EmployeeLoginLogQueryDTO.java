package com.anonym.module.log.loginlog.domain;

import com.anonym.common.domain.PageBaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class EmployeeLoginLogQueryDTO extends PageBaseDTO {

    @ApiModelProperty("开始日期")
    private String startDate;

    @ApiModelProperty("结束日期")
    private String endDate;

    @ApiModelProperty("用户名")
    private String userName;

}
