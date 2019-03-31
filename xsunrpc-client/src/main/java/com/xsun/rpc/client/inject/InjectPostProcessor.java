package com.xsun.rpc.client.inject;

import com.xsun.rpc.client.annotation.Reference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * 扫描注解，给引用字段初始化实例
 *
 * created at 20:40, 2019/3/23
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
public class InjectPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields() ;
        for(Field field : fields){
            // 若是reference注解的字段则将rpc代理类注给他
            if(field.getAnnotation(Reference.class) != null){

            }
        }
    }
}
