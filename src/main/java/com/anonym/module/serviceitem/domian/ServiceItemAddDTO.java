package com.anonym.module.serviceitem.domian;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * @author lizongliang
 * @date 2019-08-28 20:58
 */
@Data
public class ServiceItemAddDTO {

    @ApiModelProperty("服务项名字")
    @NotBlank(message = "服务项名字不能为空")
    private String itemName;

    @ApiModelProperty("单次价格")
    @Min(value = 0, message = "单次服务价格最小为0元！")
    private BigDecimal price;

    @ApiModelProperty("会员价格")
    private BigDecimal memberPrice;

    @ApiModelProperty("会员次数")
    private Integer memberPriceNum;

}
