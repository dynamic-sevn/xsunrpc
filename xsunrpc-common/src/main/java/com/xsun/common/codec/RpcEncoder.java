package com.xsun.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * created at 20:40, 2019/3/23
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
public class RpcEncoder extends MessageToByteEncoder {
    public Class<?> encoderClass ;

    public RpcEncoder(Class<?> rpcResponseClass) {
        this.encoderClass = rpcResponseClass ;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

    }
}
