package com.anonym.module.cron;

import lombok.Data;

import java.util.Date;

/**
 * @author lizongliang
 * @date 2019-10-29 9:22
 */
@Data
public class CronEntity {

    private Integer cronId;

    /**
     * cron表达式
     */
    private String cron;

    /**
     * cron描述
     */
    private String cronDesc;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;
}
