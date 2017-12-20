package com.fibbery.rpc.demo.client;

import com.fibbery.rpc.codec.RpcDecoder;
import com.fibbery.rpc.codec.RpcEncoder;
import com.fibbery.rpc.demo.api.IHelloService;
import com.fibbery.rpc.registry.Address;
import com.fibbery.rpc.registry.SubscribeMeta;
import com.fibbery.rpc.registry.ZookeeperManager;
import com.fibbery.rpc.registry.ZookeeperRegistry;
import com.fibbery.rpc.transport.message.RequestMessage;
import com.fibbery.rpc.transport.message.ResponseMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author fibbery
 * @date 17/11/30
 */
@Slf4j
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
        if (addresses == null || addresses.size() == 0) {
            log.error("can't find service");
            return;
        }
        //获取默认第一个
        Address address = addresses.get(0);

        //进行客户端请求
        Bootstrap b = new Bootstrap();
        NioEventLoopGroup g = new NioEventLoopGroup();
        final ResponseMessage responseMessage = new ResponseMessage();
        b.group(g).channel(NioSocketChannel.class).remoteAddress(address.getHost(),address.getPort()).handler(
                new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new RpcEncoder(RequestMessage.class))
                                .addLast(new RpcDecoder(ResponseMessage.class))
                                .addLast(new ClientHandler(responseMessage));
                    }
                }
        );

        ChannelFuture future = b.connect().sync();
        final CountDownLatch complete = new CountDownLatch(1);
        future.channel().writeAndFlush(message).addListener((ChannelFutureListener) channelFuture -> complete.countDown());
        complete.await();
        future.channel().closeFuture().sync();
    }
}
