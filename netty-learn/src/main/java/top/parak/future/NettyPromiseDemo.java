package top.parak.future;

import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-09-26
 * @apiNote Netty Promise
 * <p> Future只能开启线程被动接收结果，
 * Promise可以自己设置结果或者异常处理。
 */
@Slf4j(topic = "NettyPromise")
public class NettyPromiseDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        EventLoop eventLoop = group.next();
        // 结果容器
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);
        new Thread(() -> {
            log.debug("执行计算");
            try {
                int r = 1 / 0;
                TimeUnit.SECONDS.sleep(3);
                promise.setSuccess(3);
            } catch (InterruptedException e) {
                log.error("occur exception while calculating: {}", e.getMessage());
                promise.setFailure(e);
            } finally {
            }
        }).start();
        log.debug("等待结果");
        log.debug("获取结果: [{}]", promise.get());
    }
}
