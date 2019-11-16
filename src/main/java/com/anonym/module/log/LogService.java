package com.anonym.module.log;

import com.anonym.module.log.loginlog.EmployeeLoginLogDao;
import com.anonym.module.log.loginlog.domain.EmployeeLoginLogEntity;
import com.anonym.module.log.operatelog.EmployeeOperateLogDao;
import com.anonym.module.log.operatelog.domain.EmployeeOperateLogEntity;
import com.anonym.module.log.orderoperatelog.OrderOperateLogDao;
import com.anonym.module.log.orderoperatelog.domain.entity.OrderOperateLogEntity;
import com.anonym.utils.thread.SmartThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class LogService {

    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private EmployeeLoginLogDao userLoginLogDao;

    @Autowired
    private OrderOperateLogDao orderOperateLogDao;

    @Autowired
    private EmployeeOperateLogDao userOperateLogDao;

    @PostConstruct
    void init() {
        if (threadPoolExecutor == null) {
            threadPoolExecutor = new ThreadPoolExecutor(1, 1, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2000), SmartThreadFactory.create("LogAspect"));
        }
    }

    @PreDestroy
    void destroy() {
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdown();
            threadPoolExecutor = null;
        }
    }

    public void addLog(Object object) {
        try {
            if (object instanceof EmployeeLoginLogEntity) {
                threadPoolExecutor.execute(() -> userLoginLogDao.insert((EmployeeLoginLogEntity) object));
            }
            if (object instanceof OrderOperateLogEntity) {
                threadPoolExecutor.execute(() -> orderOperateLogDao.insert((OrderOperateLogEntity) object));
            }
            if (object instanceof EmployeeOperateLogEntity) {
                threadPoolExecutor.execute(() -> userOperateLogDao.insert((EmployeeOperateLogEntity) object));
            }
        } catch (Throwable e) {
            log.error("userLogAfterAdvice:{}", e);
        }
    }
}
