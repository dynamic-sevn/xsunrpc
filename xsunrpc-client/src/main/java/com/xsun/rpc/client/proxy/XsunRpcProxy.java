package com.xsun.rpc.client.proxy;

import com.xsun.common.bean.RpcRequest;
import com.xsun.rpc.registry.ServiceDiscovery;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * created at 20:40, 2019/3/23
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
@Component
public class XsunRpcProxy {
    private String serviceAddress ;
    private ServiceDiscovery serviceDiscovery ;

    public XsunRpcProxy(ServiceDiscovery serviceDiscovery){
        this.serviceDiscovery = serviceDiscovery ;
    }

    public <T> T create(Class<T> interfaceClazz){
        return create(interfaceClazz, "") ;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> interfaceClazz, String version) {

        return (T) Proxy.newProxyInstance(interfaceClazz.getClassLoader(),
                new Class[]{interfaceClazz},
                (proxy, method, args) -> {
                    RpcRequest rpcRequest = new RpcRequest() ;
                    rpcRequest.setInterfaceName(method.getDeclaringClass().getName());
                    rpcRequest.setParameters(args);
                    rpcRequest.setMethodName(method.getName());
                    rpcRequest.setServiceVersion(version);
                    rpcRequest.setParameterTypes(method.getParameterTypes());
                    rpcRequest.setRequestId(UUID.randomUUID().toString());
                    return null ;

                });
    }
}
