package com.anonym.module.member.domian;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lizongliang
 * @date 2019-08-29 10:59
 */
@Data
public class MemberVO {

    @ApiModelProperty("ID")
    private Integer memberId;

    @ApiModelProperty("服务项id")
    private Integer serviceItemId;

    @ApiModelProperty("手机")
    private String phone;

    @ApiModelProperty("名字")
    private String name;

    @ApiModelProperty("会员价格")
    private BigDecimal memberPrice;

    @ApiModelProperty("总次数")
    private Integer totalNum;

    @ApiModelProperty("剩余次数")
    private Integer unusedNum;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
