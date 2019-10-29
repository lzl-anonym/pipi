package com.anonym.module.cron;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

/**
 * @author lizongliang
 * @date 2019-10-29 9:45
 */
@EnableScheduling
@Service
public class TestService implements SchedulingConfigurer {

    @Autowired
    private CronDao cronDao;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        taskRegistrar.addTriggerTask(

                // 创建定时任务
                () -> this.test(),

                triggerContext -> {
                    // 从数据库获取执行周期
                    String cron = cronDao.queryCron(CronEnum.ORDER.getDesc());

                    // 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }

    /**
     * 订单定时任务逻辑
     */
    public void test() {
        System.out.println("订单定时任务逻辑");
    }
}
