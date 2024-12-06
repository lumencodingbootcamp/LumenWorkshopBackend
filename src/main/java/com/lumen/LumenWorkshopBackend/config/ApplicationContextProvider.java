package com.lumen.LumenWorkshopBackend.config;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) 
            throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> beanClass) {
        Map<String, T> beans = context.getBeansOfType(beanClass);
        if (beans.size() > 1) {
            // Choose specific bean or throw custom exception
            return beans.get("redisTemplate");
        }
        return context.getBean(beanClass);
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }
}
