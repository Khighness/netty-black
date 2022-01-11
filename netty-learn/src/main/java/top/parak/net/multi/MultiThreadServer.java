package top.parak.net.multi;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author KHighness
 * @since 2021-09-29
 */
@Slf4j
public class MultiThreadServer {
    public static int port = 3333;
    public static void main(String[] args) throws IOException {
        // 初始化服务器
        Thread.currentThread().setName("boss");
        ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false);
        Selector boss = Selector.open();
        server.register(boss, SelectionKey.OP_ACCEPT);
        server.bind(new InetSocketAddress(port));
        log.debug("Server started successfully at: [{}]", InetAddress.getLocalHost().getHostAddress() + ":" + port);

        // 创建固定数量的worker
        Worker[] workers = new Worker[2];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker("work-" + i);
        }
        AtomicInteger index = new AtomicInteger();

        while (true) {
            boss.select();
            Iterator<SelectionKey> iter = boss.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    SocketChannel sc = server.accept();
                    sc.configureBlocking(false);
                    // 关联selector
                    workers[index.getAndIncrement() % workers.length].register(sc);
                    log.debug("Client connected successfully: [{}]", sc.getRemoteAddress());
                }
            }
        }
    }


    /**
     * 监听读写事件
     */
    private static class Worker implements Runnable {
        private Thread thread;
        private Selector selector;
        private String name;
        private volatile boolean start = false;
        private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();

        public Worker(String name) {
            this.name = name;
        }

        public void register(SocketChannel sc) throws IOException {
            if (!start) {
                thread = new Thread(this, name);
                selector = Selector.open();
                thread.start();
                start = true;
            }
            queue.add(() -> {
                try {
                    sc.register(selector, SelectionKey.OP_READ, null);
                } catch (ClosedChannelException e) {
                    log.error("Worker occur exception: {}", e.getMessage());
                }
            });
            selector.wakeup(); // wakeup给selector增加一个信号量
        }

        @Override
        public void run() {
            while (true) {
                try {
                    selector.select(); // select消耗一个信号量
                    Runnable task = queue.poll();
                    if (task != null) {
                        task.run();
                    }
                    Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        if (key.isReadable()) {
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel sc = (SocketChannel) key.channel();
                            sc.read(buffer);
                            buffer.flip();
                            log.debug("Client[{}]: {}", sc.getRemoteAddress(), StandardCharsets.UTF_8.decode(buffer));
                        }
                        iter.remove();
                    }
                } catch (IOException e) {
                    log.error("Worker occur exception: {}", e.getMessage());
                }
            }
        }
    }
}
