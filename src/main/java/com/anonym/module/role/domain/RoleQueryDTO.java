package com.anonym.module.role.domain;

import com.anonym.common.domain.PageBaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 钢圈
 * @copyright (c) 2019 钢圈Inc. All rights reserved.
 * @date
 * @since JDK1.8
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class RoleQueryDTO extends PageBaseDTO {

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色id")
    private String roleId;
}
