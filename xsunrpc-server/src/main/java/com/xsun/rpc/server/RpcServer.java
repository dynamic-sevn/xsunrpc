package com.xsun.rpc.server;

import com.xsun.rpc.registry.ServiceRegistry;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * created at 20:40, 2019/3/23
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
public class RpcServer implements ApplicationContextAware {

    private String serviceAddress ;
    private ServiceRegistry serviceRegistry ;

    private Map<String, Object> nameServiceMap = new HashMap<>() ;

    public RpcServer(String serviceAddress, ServiceRegistry serviceRegistry){
        this.serviceAddress = serviceAddress ;
        this.serviceRegistry = serviceRegistry ;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(XsunRpc.class) ;
    }
}
