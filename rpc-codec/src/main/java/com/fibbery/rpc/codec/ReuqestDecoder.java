package com.fibbery.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author fibbery
 * @date 17/12/1
 */
public class ReuqestDecoder  extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < 4) {
            return;
        }

        int length = byteBuf.readInt();
        if (byteBuf.readableBytes() < length) {
            throw new RuntimeException("byte length has incorrect!!");
        }
        byte[] data = new byte[length];
        byteBuf.readBytes(data);

    }
}
