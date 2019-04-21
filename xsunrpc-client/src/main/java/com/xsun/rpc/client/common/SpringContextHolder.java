package com.xsun.rpc.client.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * created at 20:40, 2019/3/23
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
public class SpringContextHolder implements ApplicationContextAware {
    private static ApplicationContext applicationContext ;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext ;
    }

    public static <T> T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz) ;
    }

    public static Object getBean(String name){
        return applicationContext.getBean(name);
    }
}
