package top.parak.future;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-09-26
 * @apiNote Netty Future
 * <p> 可以同步获取结果，也可以通过监听器异步获取结果
 */
@Slf4j
public class NettyFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 创建事件循环组
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        EventLoop eventLoop= group.next();
        log.debug("提交任务");
        Future<Integer> future = eventLoop.submit(() -> {
            log.debug("执行计算");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        });
        log.debug("等待结果");

        // (1) 同步阻塞获取结果
        // log.debug("获取结果：[{}]", future.get());

        // (2) 异步监听获取结果
        future.addListener(f -> log.debug("获取结果：[{}]", future.getNow()));

        eventLoop.shutdownGracefully();
    }
}
