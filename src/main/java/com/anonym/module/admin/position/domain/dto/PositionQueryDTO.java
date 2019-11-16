package com.anonym.module.admin.position.domain.dto;

import com.anonym.common.domain.PageBaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 岗位
 */
@Data
public class PositionQueryDTO extends PageBaseDTO {

    @ApiModelProperty("岗位名称")
    private String positionName;

}
