package top.parak.net.easy;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @author KHighness
 * @since 2021-09-29
 */
@Slf4j
public class WriteServer {
    public static int port = 3333;
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(port));
        while (true) {
            selector.select();
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    SelectionKey sckey = sc.register(selector, 0, null);
                    sckey.interestOps(SelectionKey.OP_READ);

                    // 向客户端发送大量数据
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < 5000000; i++) {
                        builder.append('k');
                    }
                    ByteBuffer buffer = Charsets.UTF_8.encode(builder.toString());
                    // 返回实际写入的字节数
                    int writeBytes = sc.write(buffer);
                    log.debug("write byte count: {}", writeBytes);
                    // 存在剩余内容
                    while (buffer.hasRemaining()) {
                        // 关注可写事件
                        sckey.interestOps(sckey.interestOps() | SelectionKey.OP_WRITE);
                        // 把未写完的数据挂载到key上
                        sckey.attach(buffer);
                    }
                } else if (key.isWritable()) {
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    SocketChannel sc = (SocketChannel) key.channel();
                    int writeBytes = sc.write(buffer);
                    log.debug("write byte count: {}", writeBytes);
                    // 如果数据写完，取消关注可写事件，取消挂载buffer
                    if (!buffer.hasRemaining()) {
                        key.interestOps(key.interestOps() & SelectionKey.OP_WRITE);
                        key.attach(null);
                    }
                }
                iter.remove();
            }
        }
    }
}
