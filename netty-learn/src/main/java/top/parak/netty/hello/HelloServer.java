package top.parak.netty.hello;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author KHighness
 * @since 2021-09-30
 * @apiNote HelloWorld服务器
 */
@Slf4j
public class HelloServer {
    public static int port = 3333;
    public static void main(String[] args) {
        // boss
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // worker
        EventLoopGroup workerGroup = new NioEventLoopGroup(2 * Runtime.getRuntime().availableProcessors());
        // 服务端启动器
        new ServerBootstrap()
                // Boss Worker
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new StringDecoder());
                        p.addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("[{}]: {}", ctx.channel().remoteAddress(), msg);
                            }
                        });
                    }
                })
                .bind(port);
    }
}
