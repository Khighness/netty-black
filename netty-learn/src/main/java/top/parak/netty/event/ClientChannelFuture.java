package top.parak.netty.event;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import top.parak.netty.hello.HelloServer;

import java.net.InetSocketAddress;

/**
 * @author KHighness
 * @since 2021-10-01
 * @apiNote 客户端ChannelFuture
 */
@Slf4j
public class ClientChannelFuture {
    public static void main(String[] args) throws InterruptedException {
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new StringEncoder());
                    }
                })
        // 异步非阻塞，main线程发起调用，真正执行connect的是NioEventLoopGroup的NIO线程
        .connect(new InetSocketAddress(HelloServer.port));


        // (1) 同步阻塞，主线程等上面的NIO线程异步操作执行完毕，即connect连接成功
//        channelFuture.sync();
//        Channel channel = channelFuture.channel();
//        log.debug("channel = {}", channel);
//        channel.writeAndFlush("Hello, World");


        // (2) addListener异步处理结果，所有的操作都交给NIO线程处理
        channelFuture.addListener(new ChannelFutureListener() {
            @Override // 在NIO线程连接建立完毕后执行回调方法
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel channel = future.channel();
                log.debug("channel = {}", channel);
                channel.writeAndFlush("Hello, World");
            }
        });
    }
}
