package com.xsun.rpc.client.inject;

import com.xsun.rpc.client.annotation.Reference;
import com.xsun.rpc.client.proxy.XsunRpcProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.annotation.Resource;
import java.lang.reflect.Field;

/**
 * 扫描注解，给引用字段初始化实例
 *
 * created at 20:40, 2019/3/23
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
public class InjectPostProcessor implements BeanPostProcessor {

    @Resource
    private XsunRpcProxy xsunRpcProxy ;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields() ;
        for(Field field : fields){
            // 若是reference注解的字段则将rpc代理类注给他
            if(field.getAnnotation(Reference.class) != null){
                // TODO spring 容器里有就不用初始化了，否则初始化
                field.set(bean, xsunRpcProxy.create(field.getDeclaringClass()));
            }
        }
    }
}
