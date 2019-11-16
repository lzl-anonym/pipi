package com.anonym.module.user.basic.domain;

import com.anonym.common.domain.PageBaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 用户后管分页查询 DTO 类
 */
@Data
public class UserAdminQueryDTO extends PageBaseDTO {

    @ApiModelProperty("搜索关键字")
    @Length(max = 20, message = "搜索关键字最多20个字符")
    private String keyWord;

    @ApiModelProperty("禁用状态")
    private Boolean disabledFlag;

}
