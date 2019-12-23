package com.anonym.module.message.domain.vo;

import com.anonym.module.message.domain.dto.MessageBaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author lizongliang
 * @date 2019-12-20 18:48
 */
@Data
public class MessageAppVO extends MessageBaseDTO {

    @ApiModelProperty("留言ID")
    private Long messageId;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
