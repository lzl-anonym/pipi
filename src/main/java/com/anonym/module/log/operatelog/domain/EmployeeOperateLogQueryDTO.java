package com.anonym.module.log.operatelog.domain;

import com.anonym.common.domain.PageBaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class EmployeeOperateLogQueryDTO extends PageBaseDTO {

    @ApiModelProperty("开始日期")
    private String startDate;

    @ApiModelProperty("结束日期")
    private String endDate;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("请求结果 0失败 1成功")
    private Integer resultFlag;

}
