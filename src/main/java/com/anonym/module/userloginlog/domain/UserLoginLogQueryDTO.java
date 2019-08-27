package com.anonym.module.userloginlog.domain;

import com.anonym.common.domain.PageBaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * [ 用户登录日志 ]
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class UserLoginLogQueryDTO extends PageBaseDTO {

    @ApiModelProperty("开始日期")
    private String startDate;

    @ApiModelProperty("结束日期")
    private String endDate;

    @ApiModelProperty("用户名")
    private String userName;

}
