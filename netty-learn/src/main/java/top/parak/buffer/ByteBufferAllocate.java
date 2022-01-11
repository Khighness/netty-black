package top.parak.buffer;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

/**
 * @author KHighness
 * @since 2021-09-28
 * @apiNote ByteBuffer的内存分配
 */
@Slf4j
public class ByteBufferAllocate {
    public static void main(String[] args) {
        // 使用JVM堆内存：读写效率低，受到GC的影响
        ByteBuffer buffer = ByteBuffer.allocate(16);
        // 使用直接内存：读写效率高，使用mmap在内核缓冲区和用户缓冲区共享内存
        // 但是直接内存属于系统内存，需要系统调用进行内存分配，分配速度较慢
        // DirectBuffer必须要合理释放，释放不干净会造成内存泄露
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(16);
    }
}
