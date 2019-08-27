package com.anonym.module.privilege.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@EqualsAndHashCode(callSuper = false)
@Data
public class PrivilegeDTO extends PrivilegeAddDTO {

    @ApiModelProperty("菜单id")
    private Integer id;

    /**
     * 子菜单
     */
    @ApiModelProperty("子菜单")
    @JsonProperty("children")
    private List<PrivilegeDTO> childrenPrivilege;

    /**
     * 菜单下对应的页面集合
     */
    @ApiModelProperty("页面")
    @JsonProperty("childrenPages")
    private List<PrivilegeDTO> childrenPages;

    /**
     * 菜单下对应的功能点集合
     */
    @ApiModelProperty("功能点")
    @JsonProperty("childrenPoints")
    private List<PrivilegeDTO> childrenPoints;

}
