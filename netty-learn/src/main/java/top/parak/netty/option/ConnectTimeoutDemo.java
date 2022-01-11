package top.parak.netty.option;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author KHighness
 * @since 2021-10-05
 */
@Slf4j
public class ConnectTimeoutDemo {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 300);
            bootstrap.handler(new LoggingHandler());
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(3333)).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        } finally {
            group.shutdownGracefully();
        }
    }
}
