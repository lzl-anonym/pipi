package com.anonym.module.cron;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author lizongliang
 * @date 2019-10-29 9:21
 */
@Mapper
@Component
public interface CronDao extends BaseMapper<CronEntity> {

    /**
     * 通过定时任务描述获取cron表达式
     *
     * @param cronDesc
     * @return
     */
    String queryCron(String cronDesc);
}
