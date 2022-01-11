package top.parak.netty.event;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-10-01
 * @apiNote EventLoopGroup事件循环组
 */
@Slf4j
public class EventLoopDemo {
    public static void main(String[] args) {
        log.debug("cpus = {}", NettyRuntime.availableProcessors());
        EventLoopGroup group = new NioEventLoopGroup(2);     // io事件
        log.debug("{}", group.next());
        log.debug("{}", group.next());
        log.debug("{}", group.next());
        log.debug("{}", group.next());
        group.next().submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("可");
        });
        group.next().scheduleAtFixedRate(() -> {
            log.debug("定时任务");
        }, 0 , 3, TimeUnit.SECONDS);
        log.debug("面");
    }
}
