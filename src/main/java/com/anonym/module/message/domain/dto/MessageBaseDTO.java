package com.anonym.module.message.domain.dto;

import com.anonym.common.anno.ApiModelPropertyEnum;
import com.anonym.common.validator.en.CheckEnum;
import com.anonym.module.message.MessageTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author lizongliang
 * @date 2019-12-20 18:48
 */
@Data
public class MessageBaseDTO {

    @ApiModelPropertyEnum(MessageTypeEnum.class)
    @CheckEnum(enumClazz = MessageTypeEnum.class, message = "留言类型错误")
    private Integer messageType;

    @ApiModelProperty("留言内容")
    @Length(max = 500, message = "留言最多500个字符")
    @NotBlank(message = "留言内容不能为空")
    private String content;

    @ApiModelProperty("图片（备用）")
    private String picture;

    @ApiModelProperty("留言人id（备用）")
    @NotNull(message = "员工id不能为空")
    private Long userId;

    @ApiModelProperty("留言人名字（备用）")
    @NotBlank(message = "留言人名字不能为空")
    @Length(max = 20, message = "留言人名字最多20个字符")
    private String userName;

    @ApiModelProperty(value = "删除状态  true删除  false不删除  ", hidden = true)
    private Boolean deleteFlag;

}
