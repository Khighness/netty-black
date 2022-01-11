package top.parak.future;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author KHighness
 * @since 2021-10-01
 * @apiNote JDK future
 * <p> 只能同步阻塞获取结果
 */
@Slf4j
public class JDKFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        log.debug("提交任务");
        Future<Integer> future = executorService.submit(() -> {
            log.debug("执行计算");
            TimeUnit.SECONDS.sleep(3);
            return 1;
        });
        log.debug("等待结果");
        log.debug("获取结果：[{}]", future.get());
        executorService.shutdown();
    }
}
