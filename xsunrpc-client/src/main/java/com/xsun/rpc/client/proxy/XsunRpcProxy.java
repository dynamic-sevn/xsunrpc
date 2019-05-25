package com.xsun.rpc.client.proxy;

import com.xsun.common.bean.RpcRequest;
import com.xsun.common.bean.RpcResponse;
import com.xsun.rpc.client.remote.RpcClient;
import com.xsun.rpc.registry.ServiceDiscovery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * created at 20:40, 2019/3/23
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
public class XsunRpcProxy {
    private String serviceAddress ;
    private ServiceDiscovery serviceDiscovery ;

    private static final Logger LOG = LoggerFactory.getLogger(XsunRpcProxy.class) ;

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

                    if(serviceDiscovery != null){
                        String serviceName = interfaceClazz.getName() ;
                        if(StringUtils.isNotBlank(version)){
                            serviceName += "-" + version ;
                        }

                        serviceAddress = serviceDiscovery.discover(serviceName) ;
                        LOG.info("find service {} => {}", serviceName, serviceAddress);
                    }

                    if(serviceAddress == null){
                        throw new RuntimeException(String.format("can not find address of %s", interfaceClazz.getName())) ;
                    }

                    String[] addressArray = serviceAddress.split(":");

                    Integer port = Integer.parseInt(addressArray[1]) ;
                    RpcClient rpcClient = new RpcClient(addressArray[0], port) ;
                    RpcResponse response = rpcClient.remoteInvoke(rpcRequest) ;
                    if(response == null){
                        throw new RuntimeException("response is null") ;
                    }

                    if(response.getException() != null){
                        throw response.getException();
                    }else {
                        return response.getResult();
                    }

                });
    }
}
