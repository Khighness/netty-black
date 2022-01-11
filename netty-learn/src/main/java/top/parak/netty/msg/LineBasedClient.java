package top.parak.netty.msg;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Random;

/**
 * @author KHighness
 * @since 2021-10-01
 */
@Slf4j
public class LineBasedClient {
    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            ByteBuf buf = ctx.alloc().buffer();
                            char c = '0';
                            Random random = new Random();
                            for (int i = 0; i < 10; i++, c++) {
                                byte[] bytes = fillRandomBytes(c, 1 + random.nextInt(20));
                                buf.writeBytes(bytes);
                            }
                            ctx.writeAndFlush(buf);
                        }
                    });
                }
            });
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(FixedLengthServer.port)).sync();
            log.debug("Client connected to server successfully");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("Client occur exception: {}", e.getMessage());
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    private static byte[] fillRandomBytes(char c, int l) {
        if (l <= 0) {
            throw new IllegalArgumentException("l must be positive");
        }
        byte[] bytes = new byte[l];
        if (l >= 2)
            Arrays.fill(bytes, 0, l - 2, (byte) c);
        bytes[l - 1] = '\n';
        return bytes;
    }
}
