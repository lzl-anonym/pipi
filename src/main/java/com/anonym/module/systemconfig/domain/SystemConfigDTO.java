package com.anonym.module.systemconfig.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
public class SystemConfigDTO {

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("参数key")
    private String configKey;

    @ApiModelProperty("参数的值")
    private String configValue;

    @ApiModelProperty("参数名称")
    private String configName;

    @ApiModelProperty("参数类别")
    private String configGroup;

    @ApiModelProperty("是否使用0 是 1否")
    private Integer isUsing;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("上次修改时间")
    private Date updateTime;

}
