package com.anonym.module.file.domain.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class UploadVO {

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "fileUrl")
    private String fileUrl;

    @ApiModelProperty(value = "fileKey")
    private String fileKey;

    @ApiModelProperty(value = "文件大小")
    private Long fileSize;
}
