package com.anonym.module.fuseuser.fuse.domian;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author lizongliang
 * @date 2019-11-16 16:44
 */
@Data
public class FaceFuseVO {

	private Integer faceFuseId;

	@ApiModelProperty("用户id")
	private Integer faceFuseUserId;

	@ApiModelProperty("融合图的BASE64值")
	private String mergeImage;

	@ApiModelProperty("员工姓名")
	private String userName;

	private Date updateTime;

	private Date createTime;
}
