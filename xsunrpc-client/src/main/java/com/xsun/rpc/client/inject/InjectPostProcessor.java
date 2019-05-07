package com.xsun.rpc.client.inject;

import com.xsun.rpc.client.annotation.Reference;
import com.xsun.rpc.client.common.SpringContextHolder;
import com.xsun.rpc.client.proxy.XsunRpcProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 扫描注解，给引用字段初始化实例
 *
 * created at 20:40, 2019/3/23
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
public class InjectPostProcessor implements BeanPostProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(InjectPostProcessor.class) ;

    /**
     * 保存代理 rpc 对象
     */
    private Map<Class<?>, Object> rpcObjectMap ;

    private XsunRpcProxy xsunRpcProxy ;

    public InjectPostProcessor(XsunRpcProxy xsunRpcProxy){
        this.xsunRpcProxy = xsunRpcProxy ;
        rpcObjectMap = new ConcurrentHashMap<>() ;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields() ;
        for(Field field : fields){
            // 若是reference注解的字段则将rpc代理类注给他
            if(field.getAnnotation(Reference.class) != null){
                try {
                    // TODO spring 容器里有就不用初始化了，否则初始化
                    field.setAccessible(true);
                    if (rpcObjectMap.get(field.getType()) != null) {
                        field.set(bean, rpcObjectMap.get(field.getType()));
                    } else {
                        field.set(bean, xsunRpcProxy.create(field.getType()));
                        rpcObjectMap.put(field.getType(), bean) ;
                    }
                }catch (Exception e){
                    LOG.error("Initialize bean error. beanClass = " + field.getType(), e);
                    e.printStackTrace();
                }
            }
        }

        return bean ;
    }
}
