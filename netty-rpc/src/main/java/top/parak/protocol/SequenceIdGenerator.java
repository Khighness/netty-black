package top.parak.protocol;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生成序列号id
 *
 * @author KHighness
 */
public abstract class SequenceIdGenerator {

    private static final AtomicInteger id = new AtomicInteger();

    public static int nextId() {
        return id.incrementAndGet();
    }

}
