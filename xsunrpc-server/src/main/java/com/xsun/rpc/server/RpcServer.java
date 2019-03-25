package com.xsun.rpc.server;

import com.xsun.common.bean.RpcRequest;
import com.xsun.common.bean.RpcResponse;
import com.xsun.common.codec.RpcDecoder;
import com.xsun.common.codec.RpcEncoder;
import com.xsun.rpc.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * created at 20:40, 2019/3/23
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
public class RpcServer implements ApplicationContextAware, InitializingBean {

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
        if(MapUtils.isNotEmpty(serviceBeanMap)){
            for(Object serviceBean : serviceBeanMap.values()){
                XsunRpc xsunRpc = serviceBean.getClass().getAnnotation(XsunRpc.class) ;
                String serviceName = xsunRpc.value().getName() ;
                String serviceVersion = xsunRpc.version() ;
                if(StringUtils.isNotBlank(serviceVersion)){
                    serviceName += "-" + serviceVersion ;
                }

                nameServiceMap.put(serviceName, serviceBean) ;
            }
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup() ;
        EventLoopGroup workerGroup = new NioEventLoopGroup() ;

        ServerBootstrap bootstrap = new ServerBootstrap() ;
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline channelPipeline = ch.pipeline() ;
                        channelPipeline.addLast(new RpcDecoder(RpcRequest.class))
                                .addLast(new RpcEncoder(RpcResponse.class))
                                .addLast(new RpcServerHandler(nameServiceMap)) ;
                    }
                }) ;

    }
}
