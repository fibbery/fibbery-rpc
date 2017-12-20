package com.fibbery.rpc.demo.client;

import com.fibbery.rpc.transport.message.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author fibbery
 * @date 17/12/8
 */
public class ClientHandler extends SimpleChannelInboundHandler<ResponseMessage> {

    private ResponseMessage message;

    public ClientHandler(ResponseMessage message) {
        this.message = message;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ResponseMessage responseMessage) throws Exception {
        System.out.println("RpcClientHandler respons : " + responseMessage);
        message.setResponseId(responseMessage.getResponseId());
        message.setErrorCode(responseMessage.getErrorCode());
        message.setResult(responseMessage.getResult());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public ResponseMessage getMessage() {
        return message;
    }
}
