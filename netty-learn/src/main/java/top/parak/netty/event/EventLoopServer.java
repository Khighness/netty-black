package top.parak.netty.event;

import com.google.common.base.Charsets;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author KHighness
 * @since 2021-10-01
 * @apiNote EventLoop执行IO任务
 */
@Slf4j
public class EventLoopServer {
    public static void main(String[] args) {
        // boss: 处理连接
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // worker：处理读写
        EventLoopGroup workerGroup = new NioEventLoopGroup(2);
        // 处理特定Handler
        EventLoopGroup loopGroup = new DefaultEventLoopGroup();
        new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast("handler1", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                log.debug("[{}]: {}", ctx.channel().remoteAddress(), buf.toString(Charsets.UTF_8));
                                ctx.fireChannelRead(msg); // 将消息传递给下一个Handler
                            }
                        });
                        p.addLast(loopGroup, "handler2", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                log.debug("[{}]: {}", ctx.channel().remoteAddress(), buf.toString(Charsets.UTF_8));
                            }
                        });
                    }
                })
                .bind(3333);
    }
}
