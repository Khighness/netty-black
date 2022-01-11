package top.parak.netty.event;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * @author KHighness
 * @since 2021-10-01
 * @apiNote 输入quit关闭ChannelFuture，进行善后操作
 */
@Slf4j
public class CloseChannelFuture {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new LoggingHandler(LogLevel.DEBUG));
                        p.addLast(new StringEncoder());
                    }
                })
                .connect(new InetSocketAddress(3333));
        Channel channel = channelFuture.sync().channel();
        log.debug("Client connected to server successfully: [{}]", channel.remoteAddress());
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.equals("quit")) {
                    channel.close();
                    break;
                } else {
                    channel.writeAndFlush(line);
                }
            }
        }, "input").start();

        // 获取CloseFuture对象，可以同步处理关闭也可以异步处理关闭
        ChannelFuture closeFuture = channel.closeFuture();

        // (1) 同步阻塞，main线程处理
//        closeFuture.sync();
//        log.debug("客户端关闭");

        // (2) 异步处理，NIO线程处理
        closeFuture.addListener((ChannelFutureListener) future -> {
            log.debug("客户端关闭");
            group.shutdownGracefully(); // 优雅关闭
        });
    }
}
