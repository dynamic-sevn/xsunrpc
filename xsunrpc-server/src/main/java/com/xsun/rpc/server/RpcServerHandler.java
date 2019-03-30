package com.xsun.rpc.server;

import com.xsun.common.bean.RpcRequest;
import com.xsun.common.bean.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * created at 20:40, 2019/3/23
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private Map<String, Object> nameServiceMap ;

    public RpcServerHandler(Map<String, Object> nameServiceMap){
        this.nameServiceMap = nameServiceMap ;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        String serviceName = request.getInterfaceName() ;
        if(StringUtils.isNotBlank(request.getServiceVersion())){
            serviceName += "-" + request.getServiceVersion() ;
        }
        Object serviceObj = nameServiceMap.get(serviceName) ;
        if(serviceObj == null){
            throw new RuntimeException(String.format("Can not find the service %s ", serviceName)) ;
        }
        RpcResponse rpcResponse = new RpcResponse();
        try {
            Class<?> clazz = serviceObj.getClass();
            Class<?>[] parameterTypes = request.getParameterTypes();
            Object[] parameters = request.getParameters();
            Method method = clazz.getMethod(request.getMethodName(), parameterTypes);
            Object result = method.invoke(serviceObj, parameters);
            rpcResponse.setResult(result);
        }catch (Exception e){
            rpcResponse.setException(e);
        }

        ctx.writeAndFlush(rpcResponse).addListener(ChannelFutureListener.CLOSE) ;

    }
}
