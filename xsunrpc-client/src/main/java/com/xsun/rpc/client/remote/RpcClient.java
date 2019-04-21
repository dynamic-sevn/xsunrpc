package com.xsun.rpc.client.remote;

import com.xsun.common.bean.RpcRequest;
import com.xsun.common.bean.RpcResponse;
import com.xsun.common.codec.RpcDecoder;
import com.xsun.common.codec.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * created at 20:40, 2019/3/23
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {

    private final static Logger LOG = LoggerFactory.getLogger(RpcClient.class) ;
    private String address ;
    private Integer port ;

    private RpcResponse rpcResponse ;

    public RpcClient(String address, Integer port){
        this.address = address ;
        this.port = port ;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        this.rpcResponse = msg ;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("rpc invoke exception", cause);
        ctx.close();
    }

    public RpcResponse remoteInvoke(RpcRequest rpcRequest) throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline()
                                    .addLast(new RpcEncoder(RpcRequest.class))
                                    .addLast(new RpcDecoder(RpcResponse.class))
                                    .addLast(RpcClient.this);
                        }
                    });
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            ChannelFuture channelFuture = bootstrap.connect(address, port).sync();
            Channel channel = channelFuture.channel();
            channel.writeAndFlush(rpcRequest).sync();
            channel.closeFuture().sync();

            return rpcResponse;
        }finally {
            eventLoopGroup.shutdownGracefully();
        }

    }


}
