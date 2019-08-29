package com.anonym.module.member.domian;

import com.anonym.utils.SmartVerificationUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * @author lizongliang
 * @date 2019-08-29 11:52
 */
@Data
public class MemberUpdateDTO {


    @ApiModelProperty("用户id")
    @NotNull(message = "用户id不能为空")
    private Integer memberId;

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

    @ApiModelProperty(value = "会员价格")
    @DecimalMin(value = "0", message = "会员价最小为0元")
    private BigDecimal memberPrice;

    @ApiModelProperty(value = "总次数")
    @NotNull(message = "总次数不能为空")
    @Min(value = 0, message = "总次数最小为0次")
    private Integer totalNum;

    @ApiModelProperty(value = "剩余次数")
    @NotNull(message = "剩余次数不能为空")
    @Min(value = 0, message = "剩余次数最小为0次")
    private Integer unusedNum;
}
