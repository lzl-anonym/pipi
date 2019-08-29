package com.anonym.module.member.domian;

import com.anonym.utils.SmartVerificationUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 * @author lizongliang
 * @date 2019-08-29 11:35
 */
@Data
public class MemberAddDTO {

    @ApiModelProperty("服务项id")
    @NotNull(message = "服务项id不能为空")
    private Integer serviceItemId;

    @ApiModelProperty("手机")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = SmartVerificationUtil.PHONE_REGEXP, message = "手机号格式不正确")
    private String phone;

    @ApiModelProperty("名字")
    @NotBlank(message = "名字不能为空")
    private String name;

    @ApiModelProperty(value = "会员价格", hidden = true)
    private BigDecimal memberPrice;

    @ApiModelProperty(value = "总次数", hidden = true)
    private Integer totalNum;

    @ApiModelProperty(value = "剩余次数", hidden = true)
    private Integer unusedNum;


}
