package com.xsun.common.codec;

import com.xsun.common.bean.RpcRequest;
import com.xsun.common.bean.RpcResponse;
import com.xsun.common.util.SerializationUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * created at 20:40, 2019/3/23
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
public class RpcDecoder extends ByteToMessageDecoder {
    private Class<?> decoderClass ;

    public RpcDecoder(Class<?> rpcRequestClass) {
        this.decoderClass = rpcRequestClass ;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 必须有4个字节记录数据长度
        if(in.readableBytes() < 4){
            return ;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if(in.readableBytes() < dataLength){
            in.resetReaderIndex();
            return;
        }

        byte[] data = new byte[dataLength] ;
        in.readBytes(data);
        out.add(SerializationUtils.deserialize(data, decoderClass)) ;
    }
}
