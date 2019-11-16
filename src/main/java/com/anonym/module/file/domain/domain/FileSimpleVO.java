package com.anonym.module.file.domain.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件简单VO
 */
@Data
public class FileSimpleVO {

    @ApiModelProperty("文件Key")
    private String fileKey;

    @ApiModelProperty("文件Url")
    private String fileUrl;

}
