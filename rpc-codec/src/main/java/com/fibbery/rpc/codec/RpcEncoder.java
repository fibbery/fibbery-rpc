package com.fibbery.rpc.codec;

import com.fibbery.rpc.serialzation.KryoSerializer;
import com.fibbery.rpc.serialzation.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author fibbery
 * @date 17/12/8
 */
public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> clazz;

    private Serializer serializer;

    public RpcEncoder(Class<?> clazz) {
        this.clazz = clazz;
        this.serializer = new KryoSerializer();
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        byte[] bytes = serializer.serialize(o);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }
}
