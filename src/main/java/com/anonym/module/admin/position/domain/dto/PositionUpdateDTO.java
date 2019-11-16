package com.anonym.module.admin.position.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 岗位
 */
@Data
public class PositionUpdateDTO extends PositionAddDTO {

    @ApiModelProperty("主键")
    private Long id;
}
