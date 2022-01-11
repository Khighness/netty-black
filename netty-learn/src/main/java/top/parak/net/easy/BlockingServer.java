package top.parak.net.easy;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author KHighness
 * @since 2021-09-28
 * @apiNote NIO阻塞服务器
 */
@Slf4j
public class BlockingServer {
    public static int port = 3333;
    public static void main(String[] args) throws IOException {
        // 创建服务器套接字
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 设置为非阻塞
        ssc.configureBlocking(false);
        // 绑定地址和端口
        ssc.bind(new InetSocketAddress(3333));
        log.debug("Server started at: [{}]", InetAddress.getLocalHost().getHostAddress() + ":" + port);
        // 创建客户端套接字集合
        List<SocketChannel> channels = new ArrayList<>();
        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(128);
        while (true) {
            // 监听客户端连接
            SocketChannel sc = ssc.accept();
            if (sc != null) {
                channels.add(sc);
                log.debug("Client connected successfully: [{}]", sc.getRemoteAddress().toString().substring(1));
            }
            for (SocketChannel channel : channels) {
                // 读取客户端消息
                channel.read(buffer);
                buffer.flip();
                log.debug("{}: {}", channel.getRemoteAddress(), new String(buffer.array(), StandardCharsets.UTF_8));
                buffer.clear();
            }
        }
    }
}
