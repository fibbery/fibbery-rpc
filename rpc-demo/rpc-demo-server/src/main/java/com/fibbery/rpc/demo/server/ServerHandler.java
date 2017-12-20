package com.fibbery.rpc.demo.server;

import com.fibbery.rpc.transport.message.RequestMessage;
import com.fibbery.rpc.transport.message.ResponseMessage;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

/**
 * @author fibbery
 * @date 17/12/20
 */
public class ServerHandler extends SimpleChannelInboundHandler<RequestMessage> {

    private HashMap<String, Object> servicesMap;

    public ServerHandler(HashMap<String, Object> servicesMap) {
        this.servicesMap = servicesMap;
    }

    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage requestMessage) throws Exception {
        String className = requestMessage.getClassName();
        Object o = servicesMap.get(className);
        ResponseMessage message = new ResponseMessage();
        message.setResult(handle(requestMessage));
        message.setErrorCode(0);
        ctx.writeAndFlush(message).addListener(ChannelFutureListener.CLOSE);
    }

    private Object handle(RequestMessage requestMessage) {
        Object instance = servicesMap.get(requestMessage.getClassName());
        if (instance == null) {
            //执行啥呢？？
            return null;
        }

        Method[] declaredMethods = instance.getClass().getDeclaredMethods();
        Object result = null;
        try {
            for (Method declaredMethod : declaredMethods) {
                if (declaredMethod.getName().equals(requestMessage.getMethodName())) {
                    boolean isFit = true;
                    Class[] parameterTypes = requestMessage.getParameterTypes();
                    for (Class clazz : requestMessage.getParameterTypes()) {
                        Optional<Class> exist = Arrays.stream(parameterTypes).filter(parameterType -> {
                            if (parameterType == clazz) return true;
                            return false;
                        }).findFirst();
                        if (!exist.isPresent()) {
                            isFit = false;
                            break;
                        }
                    }
                    if (isFit) {
                        result = declaredMethod.invoke(instance, requestMessage.getParamters());
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
