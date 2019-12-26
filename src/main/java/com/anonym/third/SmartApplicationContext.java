package com.anonym.third;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
public class SmartApplicationContext implements ApplicationContextAware {


    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SmartApplicationContext.applicationContext == null) {

            SmartApplicationContext.applicationContext = applicationContext;

        }
    }


    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    public static Object getBean(String name) {
        ApplicationContext applicationContext = getApplicationContext();
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(name);
    }


    public static <T> T getBean(Class<T> clazz) {
        ApplicationContext applicationContext = getApplicationContext();
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(clazz);
    }


    public static <T> T getBean(String name, Class<T> clazz) {
        ApplicationContext applicationContext = getApplicationContext();
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(name, clazz);
    }
}