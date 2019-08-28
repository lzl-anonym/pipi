package com.anonym.module.serviceitem.domian;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lizongliang
 * @date 2019-08-28 20:29
 */
@Data
@TableName("t_service_item")
public class ServiceItemEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer serviceItemId;

    /**
     * 服务项名字
     */
    private String itemName;

    /**
     * 单次价格
     */
    private BigDecimal price;

    /**
     * 会员价格
     */
    private BigDecimal memberPrice;

    /**
     * 会员次数
     */
    private Integer memberPriceNum;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;
}
