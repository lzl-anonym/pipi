package com.anonym.module.zteuser.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author lizongliang
 * @date 2019-12-27 10:53
 */
@Data
public class ZteLoginDTO {


    @ApiModelProperty("中兴用户id")
    @NotNull(message = "中兴用户id不能为空")
    private Long zteUserId;

    @ApiModelProperty("姓名")
    @NotBlank(message = "姓名不能为空")
    private String name;
}
