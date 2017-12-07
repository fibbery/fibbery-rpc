package com.fibbery.rpc.demo.client;

import com.fibbery.rpc.demo.api.IHelloService;
import com.fibbery.rpc.registry.Address;
import com.fibbery.rpc.registry.SubscribeMeta;
import com.fibbery.rpc.registry.ZookeeperManager;
import com.fibbery.rpc.registry.ZookeeperRegistry;
import com.fibbery.rpc.transport.message.RequestMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.List;

/**
 * @author fibbery
 * @date 17/11/30
 */
public class DemoClient {

    public static void main(String[] args) throws InterruptedException {
        RequestMessage message = new RequestMessage();
        message.setClassName(IHelloService.class.getCanonicalName());
        message.setMethodName("getHelloStr");
        message.setParameterTypes(new Class[]{String.class});
        message.setParamters(new Object[]{"Jonh"});

        //连接zookeeper获取服务
        ZookeeperManager manager = new ZookeeperManager("127.0.0.1:2181");
        ZookeeperRegistry registry = new ZookeeperRegistry(manager);

        SubscribeMeta meta = new SubscribeMeta();
        meta.setServiceName(IHelloService.class.getCanonicalName());
        meta.setGroup("default");
        meta.setVersion("1.0");
        List<Address> addresses = registry.searchServiceAddress(meta);
        //获取默认第一个
        Address address = addresses.get(0);

        //进行客户端请求
        Bootstrap b = new Bootstrap();
        NioEventLoopGroup g = new NioEventLoopGroup();
        b.group(g).channel(NioSocketChannel.class).remoteAddress(address.getHost(),address.getPort()).handler(
                new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast();
                    }
                }
        );

        ChannelFuture future = b.connect().sync();
        future.channel().close();
    }
}
