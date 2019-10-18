package com.anonym.module.test;

import com.anonym.utils.date.SmartDateUtil;

import java.util.Date;

/**
 * @author lizongliang
 * @date 2019-10-14 17:27
 */
public class Test {
    public static void main(String[] args) {
        // 获取 置顶日期后的两个月的第一天
        System.out.println(SmartDateUtil.getMonthBegin(SmartDateUtil.getAfterMonth(new Date(), 2)));

    }
}
