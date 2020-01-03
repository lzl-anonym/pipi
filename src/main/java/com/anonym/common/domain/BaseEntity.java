package com.anonym.common.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;


@Data
public class BaseEntity {


    @TableId(type = IdType.AUTO)
    private Long id;


    private Date updateTime;


    private Date createTime;

}
