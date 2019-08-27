package com.anonym.module.userloginlog;

import com.anonym.module.userloginlog.domain.UserLoginLogEntity;
import com.anonym.utils.thread.SmartThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * [  ]
 */
@Service
public class LogService {

    private static final Logger LOG = LoggerFactory.getLogger(LogService.class);

    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private UserLoginLogDao userLoginLogDao;


    @PostConstruct
    void init() {
        if (threadPoolExecutor == null) {
            int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
            threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, corePoolSize, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2000), SmartThreadFactory.create("LogAspect"));
        }
    }

    @PreDestroy
    void destroy() {
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdown();
        }
    }

    public void addLog(Object object) {
        try {
            if (object instanceof UserLoginLogEntity) {
                threadPoolExecutor.execute(() -> {
                    userLoginLogDao.insert((UserLoginLogEntity) object);
                });
            }
//            if (object instanceof OrderOperateLogEntity) {
//                threadPoolExecutor.execute(() -> {
//                    orderOperateLogDao.insert((OrderOperateLogEntity) object);
//                });
//            }
//            if (object instanceof UserOperateLogEntity) {
//                threadPoolExecutor.execute(() -> {
//                    userOperateLogDao.insert((UserOperateLogEntity) object);
//                });
//        }
        } catch (
                Throwable e) {
            e.printStackTrace();
            LOG.error("userLogAfterAdvice:{}", e);
        }
    }
}
