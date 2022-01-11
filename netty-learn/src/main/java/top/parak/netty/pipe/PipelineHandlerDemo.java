package top.parak.netty.pipe;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author KHighness
 * @since 2021-10-01
 * @apiNote pipeline的处理顺序
 * <p> 入站处理器处理顺序与添加顺序相同
 * <p> 出栈处理器处理顺序与添加顺序相反
 */
@Slf4j
public class PipelineHandlerDemo {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 1. 获取pipeline
                        ChannelPipeline p = ch.pipeline();
                        // 2. 添加处理器 head - h1(in) - h2(in) - h3(in) - h4(out) - h5(out) - h6(out) - tail
                        p.addLast("h1", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("handler1");
                                ByteBuf buf = (ByteBuf) msg;
                                String str = buf.toString(StandardCharsets.UTF_8);
                                super.channelRead(ctx, str);
                            }
                        });
                        p.addLast("h2", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("handler2");
                                String name = (String) msg;
                                A a = new A(name);
                                super.channelRead(ctx, a);
                            }
                        });
                        p.addLast("h3", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("handler3");
                                log.debug("class: [{}], result: [{}]", msg.getClass(), msg.toString());
                                ch.writeAndFlush(ctx.alloc().buffer().writeBytes("OK".getBytes(StandardCharsets.UTF_8)));
                            }
                        });
                        p.addLast("h4", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("handler4");
                                super.write(ctx, msg, promise);
                            }
                        });
                        p.addLast("h5", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("handler5");
                                super.write(ctx, msg, promise);
                            }
                        });
                        p.addLast("h6", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("handler6");
                                super.write(ctx, msg, promise);
                            }
                        });
                    }
                })
                .bind(3333);
    }

    @AllArgsConstructor
    @ToString
    static class A {
        private String name;
    }
}
