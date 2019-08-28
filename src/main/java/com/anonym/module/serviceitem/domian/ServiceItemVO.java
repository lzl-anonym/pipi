package com.anonym.module.serviceitem.domian;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lizongliang
 * @date 2019-08-28 20:48
 */
@Data
public class ServiceItemVO {

    @TableId(type = IdType.AUTO)
    private Integer serviceItemId;

    @ApiModelProperty("服务项名字")
    private String itemName;

    @ApiModelProperty("单次价格")
    private BigDecimal price;

    @ApiModelProperty("会员价格")
    private BigDecimal memberPrice;

    @ApiModelProperty("会员次数")
    private Integer memberPriceNum;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
