package com.anonym.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class PageResultDTO<T> {


    @ApiModelProperty(value = "当前页")
    private Integer pageNum;

    @ApiModelProperty(value = "每页的数量")
    private Integer pageSize;


    @ApiModelProperty(value = "总记录数")
    private Long total;

    @ApiModelProperty(value = "总页数")
    private Integer pages;


    @ApiModelProperty(value = "结果集")
    private List<T> list;

}
