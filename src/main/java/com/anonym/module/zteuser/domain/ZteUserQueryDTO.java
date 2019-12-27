package com.anonym.module.zteuser.domain;

import com.anonym.common.domain.PageBaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lizongliang
 * @date 2019-12-27 10:47
 */
@Data
public class ZteUserQueryDTO extends PageBaseDTO {


    @ApiModelProperty("开始日期")
    private String startDate;

    @ApiModelProperty("结束日期")
    private String endDate;


    @ApiModelProperty("姓名")
    private String name;

}
