package top.parak.net.easy;

import lombok.extern.slf4j.Slf4j;
import top.parak.buffer.ByteBufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @author KHighness
 * @since 2021-09-28
 * @apiNote NIO非阻塞服务器
 */
@Slf4j
public class NonBockingServer {
    public static int port = 3333;
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        // 将selector注册到channel中
        SelectionKey sscKey = ssc.register(selector, 0, null);
        // 标识key只关注accept事件
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        log.debug("register key: {}", sscKey);

        ssc.bind(new InetSocketAddress(port));
        while (true) {
            // 没有事件发生，阻塞
            // 水平触发，有事件未处理时会变成非阻塞
            // 事件发生后，要么处理，要么取消
            selector.select();

            // 获取事件迭代器
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                // 获取事件
                SelectionKey key = iter.next();

                // 区分事件类型
                if (key.isAcceptable()) { // accept事件
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    // 将byteBuffer作为附件关联到selectionKey上
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    SelectionKey scKey = sc.register(selector, 0, buffer);
                    scKey.interestOps(SelectionKey.OP_READ);
                    log.debug("client connected successfully: {}", sc);
                } else if (key.isReadable()) { // read事件
                    try {
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        int read = sc.read(buffer);
                        if (read == -1) { // 客户端正常断开
                            key.cancel();
                        } else {
                            // buffer.flip();
                            // log.debug("{}: {}", sc.getRemoteAddress(), StandardCharsets.UTF_8.decode(buffer));
                            split(buffer);
                            if (buffer.position() == buffer.limit()) {
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                key.attach(newBuffer);
                            }
                        }
                    } catch (IOException e) { // 防止客户端异常关闭，导致read异常
                        log.error("occur exception: {}", e.getMessage());
                        // 客户端异常，删除key
                        key.cancel();
                    }
                }

                // 处理完key一定要从selectedKeys集合中移除
                iter.remove();
            }
        }
    }

    private static void split(ByteBuffer source) {
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            if (source.get(i) == '\n') {
                int len = i + 1 - source.position();
                ByteBuffer target = ByteBuffer.allocate(len);
                for (int j = 0; j < len; j++) {
                    target.put(source.get());
                }
                ByteBufferUtil.debugAll(target);
            }
        }
        source.compact();
    }
}
