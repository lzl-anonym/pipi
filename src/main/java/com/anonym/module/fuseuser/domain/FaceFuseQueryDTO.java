package com.anonym.module.fuseuser.domain;

import com.anonym.common.domain.PageBaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lizongliang
 * @date 2019-11-16 15:09
 */
@Data
public class FaceFuseQueryDTO extends PageBaseDTO {

	@ApiModelProperty("员工ID或者姓名")
	private String Keyword;

}
