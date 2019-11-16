package com.anonym.module.file.domain.domain;

import com.anonym.common.anno.ApiModelPropertyEnum;
import com.anonym.common.domain.PageBaseDTO;
import com.anonym.common.validator.en.CheckEnum;
import com.anonym.module.file.constant.FileFolderTypeEnum;
import com.anonym.module.file.constant.FileServiceTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 文件信息查询dto
 */
@Data
public class FileQueryDTO extends PageBaseDTO {

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "业务类型")
    @ApiModelPropertyEnum(FileFolderTypeEnum.class)
    @CheckEnum(enumClazz = FileFolderTypeEnum.class, message = "文件业务类型错误")
    private Integer moduleType;

    @ApiModelProperty(value = "文件位置")
    @ApiModelPropertyEnum(FileServiceTypeEnum.class)
    @CheckEnum(enumClazz = FileServiceTypeEnum.class, message = "文件位置类型错误")
    private Integer fileLocationType;

}
