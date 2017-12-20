package com.fibbery.rpc.codec;

import com.fibbery.rpc.serialzation.KryoSerializer;
import com.fibbery.rpc.serialzation.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author fibbery
 * @date 17/12/8
 */
public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> clazz;

    private Serializer serializer;

    public RpcDecoder(Class<?> clazz) {
        this.clazz = clazz;
        this.serializer = new KryoSerializer();
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < 4) {
            return;
        }

        int length = byteBuf.readInt();
        if (byteBuf.readableBytes() < length) {
            throw new RuntimeException("data package has broken!!!");
        }

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        Object obj= serializer.deserialize(bytes, clazz);
        list.add(obj);
    }
}
