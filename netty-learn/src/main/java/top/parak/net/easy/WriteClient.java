package top.parak.net.easy;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author KHighness
 * @since 2021-09-29
 */
@Slf4j
public class WriteClient {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress(WriteServer.port));
        // 接收数据
        int readBytes = 0;
        while (true) {
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
            readBytes += sc.read(buffer);
            log.debug("all read byte count: {}", readBytes);
            buffer.clear();
        }
    }
}
