package com.hapicc.common.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHelper.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(String name, @Nullable Class<T> clz) {
        return applicationContext.getBean(name, clz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name, Object... args) {
        return (T) applicationContext.getBean(name, args);
    }

    public static <T> T getBean(Class<T> clz) {
        return applicationContext.getBean(clz);
    }

    public static <T> T getBean(Class<T> clz, Object... args) {
        return applicationContext.getBean(clz, args);
    }
}
