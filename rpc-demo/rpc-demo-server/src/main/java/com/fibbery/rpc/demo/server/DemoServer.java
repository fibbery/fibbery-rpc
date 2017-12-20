package com.fibbery.rpc.demo.server;

import com.fibbery.rpc.codec.RpcDecoder;
import com.fibbery.rpc.codec.RpcEncoder;
import com.fibbery.rpc.demo.api.IHelloService;
import com.fibbery.rpc.demo.server.service.HelloServiceImpl;
import com.fibbery.rpc.registry.*;
import com.fibbery.rpc.transport.message.RequestMessage;
import com.fibbery.rpc.transport.message.ResponseMessage;
import com.google.common.collect.Maps;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.HashMap;

/**
 * @author fibbery
 * @date 17/11/30
 */
public class DemoServer {

    public static void main(String[] args) throws InterruptedException {
        //服务端有一个服务类的集合,里面装在了各种服务类的单例
        HashMap<String, Object> services = Maps.newHashMap();
        services.put(IHelloService.class.getCanonicalName(), new HelloServiceImpl());


        EventLoopGroup g = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(g).channel(NioServerSocketChannel.class).localAddress(9999).childHandler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new RpcEncoder(ResponseMessage.class))
                        .addLast(new RpcDecoder(RequestMessage.class))
                        .addLast(new ServerHandler(services));

            }
        });


        ZookeeperManager manager = new ZookeeperManager("127.0.0.1:2181");
        ZookeeperRegistry registry = new ZookeeperRegistry(manager);

        PublishMeta publishInfo = new PublishMeta();
        publishInfo.setAddress(new Address("10.1.12.90", 9999));
        SubscribeMeta serviceInfo = new SubscribeMeta();
        serviceInfo.setServiceName(IHelloService.class.getCanonicalName());
        serviceInfo.setGroup("default");
        serviceInfo.setVersion("1.0");
        publishInfo.setSubscribeMeta(serviceInfo);
        registry.publish(publishInfo);

        ChannelFuture future = b.bind().sync();
        future.channel().closeFuture().sync();
    }
}
