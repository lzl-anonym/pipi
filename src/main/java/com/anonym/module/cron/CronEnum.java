package com.anonym.module.cron;

import com.anonym.common.domain.BaseEnum;

/**
 * cron 常量
 *
 * @author lizongliang
 * @date 2019-10-29 9:36
 */
public enum CronEnum implements BaseEnum {


    /**
     * 订单定时任务
     */
    ORDER("0/5 * * * * ?", "订单定时任务");


    private String cron;

    private String desc;


    CronEnum(String cron, String desc) {
        this.cron = cron;
        this.desc = desc;
    }

    @Override
    public String getValue() {
        return cron;
    }

    @Override
    public String getDesc() {
        return desc;
    }}
