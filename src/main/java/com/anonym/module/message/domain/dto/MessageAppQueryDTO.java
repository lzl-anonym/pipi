package com.anonym.module.message.domain.dto;

import com.anonym.common.anno.ApiModelPropertyEnum;
import com.anonym.common.domain.PageBaseDTO;
import com.anonym.module.message.MessageTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lizongliang
 * @date 2019-12-20 18:48
 */
@Data
public class MessageAppQueryDTO extends PageBaseDTO {


	@ApiModelProperty("开始日期")
	private String startDate;

	@ApiModelProperty("结束日期")
	private String endDate;

	@ApiModelProperty("员工ID或者名字")
	private String keyword;

	@ApiModelProperty("留言内容")
	private String content;

	@ApiModelPropertyEnum(MessageTypeEnum.class)
	private Integer messageType;

	@ApiModelProperty(value = "删除状态", hidden = true)
	private Boolean deleteFlag;

}
