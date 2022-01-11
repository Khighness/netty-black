package top.parak.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import top.parak.config.ServerConfig;
import top.parak.protocol.MessageCodecSharable;
import top.parak.protocol.ProtocolFrameDecoder;
import top.parak.server.handler.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author KHighness
 * @since 2021-10-03
 */
@Slf4j
public class ChatServer {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable messageCodec = new MessageCodecSharable();
        LoginHandler loginHandler = new LoginHandler();
        ChatHandler chatHandler = new ChatHandler();
        GroupCreateHandler groupCreateHandler = new GroupCreateHandler();
        GroupChatHandler groupChatHandler = new GroupChatHandler();
        GroupJoinHandler groupJoinHandler = new GroupJoinHandler();
        GroupQuitHandler groupQuitHandler = new GroupQuitHandler();
        GroupMembersHandler groupMembersHandler = new GroupMembersHandler();
        QuitHandler quitHandler = new QuitHandler();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new ProtocolFrameDecoder());
                    p.addLast(loggingHandler);
                    p.addLast(messageCodec);
                    p.addLast(new ServerStateHandler());
                    p.addLast(new ServerDuplexHandler());
                    p.addLast(loginHandler);
                    p.addLast(chatHandler);
                    p.addLast(groupCreateHandler);
                    p.addLast(groupChatHandler);
                    p.addLast(groupJoinHandler);
                    p.addLast(groupQuitHandler);
                    p.addLast(groupMembersHandler);
                    p.addLast(quitHandler);
                }
            });
            int serverPort = ServerConfig.getServerPort();
            ChannelFuture channelFuture = serverBootstrap.bind(serverPort).sync();
            log.info("Server started successfully at [{}]",
                    InetAddress.getLocalHost().getHostAddress() + ":" + serverPort);
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException | UnknownHostException e) {
            log.error("Server occur exception: {}", e.getMessage());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
