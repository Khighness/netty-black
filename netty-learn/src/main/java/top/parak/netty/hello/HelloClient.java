package top.parak.netty.hello;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author KHighness
 * @since 2021-09-30
 * @apiNote HelloWorld客户端
 */
@Slf4j
public class HelloClient {
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
                // 异步非阻塞，main线程发起调用，真正执行connect的是NioEventLoopGroup的线程
                .connect(new InetSocketAddress(HelloServer.port))
                // 同步阻塞，等上面的异步操作执行完毕，即connect连接成功
                .sync();
        Channel channel = channelFuture.channel();
        log.debug("channel = {}", channel);
        channel.writeAndFlush("Hello, World");
    }
}
