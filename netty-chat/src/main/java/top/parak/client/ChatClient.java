package top.parak.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import top.parak.client.handler.BusinessHandler;
import top.parak.client.handler.ClientDuplexHandler;
import top.parak.client.handler.ClientStateHandler;
import top.parak.config.ServerConfig;
import top.parak.protocol.MessageCodecSharable;
import top.parak.protocol.ProtocolFrameDecoder;

import java.net.InetSocketAddress;

/**
 * @author KHighness
 * @since 2021-10-04
 */
@Slf4j
public class ChatClient {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable messageCodec = new MessageCodecSharable();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new ProtocolFrameDecoder());
                    p.addLast(loggingHandler);
                    p.addLast(messageCodec);
                    p.addLast(new ClientStateHandler());
                    p.addLast(new ClientDuplexHandler());
                    p.addLast(new BusinessHandler());
                }
            });
            ChannelFuture channelFuture = bootstrap.connect(
                    new InetSocketAddress(ServerConfig.getServerPort())).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("Client occur exception: {}", e.getMessage());
        } finally {
            group.shutdownGracefully();
        }
    }
}
