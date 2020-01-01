package com.anonym.module.fuseuser.fuse.domian;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author lizongliang
 * @date 2019-11-16 16:49
 */
@Data
public class FaceFuseDeleteDTO {

	@ApiModelProperty("融合结果id集合")
	@Size(min = 1, message = "最少一个融合结果idid")
	private List<Long> faceFuseIdList;

}
