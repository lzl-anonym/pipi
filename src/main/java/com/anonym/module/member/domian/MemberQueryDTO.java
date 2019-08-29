package com.anonym.module.member.domian;

import com.anonym.common.domain.PageBaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author lizongliang
 * @date 2019-08-29 10:59
 */
@Data
public class MemberQueryDTO extends PageBaseDTO {

    @ApiModelProperty("手机")
    private String phone;

    @ApiModelProperty("名字")
    private String name;

    @ApiModelProperty("开始时间")
    private Date startDate;

    @ApiModelProperty("结束时间")
    private Date endDate;
}
