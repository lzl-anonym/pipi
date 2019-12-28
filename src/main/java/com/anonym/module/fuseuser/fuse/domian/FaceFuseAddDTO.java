package com.anonym.module.fuseuser.fuse.domian;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author lizongliang
 * @date 2019-11-16 16:49
 */
@Data
public class FaceFuseAddDTO {

    @ApiModelProperty("模板base64串")
    @NotBlank(message = "模板base64串不能为空")
    private String templateBase64;

    @ApiModelProperty("人物base64串")
    @NotBlank(message = "人物base64串不能为空")
    private String imageBase64;

}
