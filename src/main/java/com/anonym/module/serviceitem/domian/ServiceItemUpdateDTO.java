package com.anonym.module.serviceitem.domian;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author lizongliang
 * @date 2019-08-28 21:14
 */
@Data
public class ServiceItemUpdateDTO extends ServiceItemAddDTO {

    @ApiModelProperty("ID")
    @NotNull(message = "ID不允许为空！")
    private Integer serviceItemId;
}
