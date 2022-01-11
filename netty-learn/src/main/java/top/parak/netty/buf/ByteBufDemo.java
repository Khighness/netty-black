package top.parak.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author KHighness
 * @since 2021-10-01
 * @apiNote ByteBuf创建
 * <p> 可以声明式使用堆内存/直接内存。
 * 声明使用堆内存，分配快，读写慢，受GC影响；
 * 声明使用直接内存，分配慢，读写快，大小受限于OS内存。
 *
 * <pre> {@code
 *     ByteBufAllocator.DEFAULT.heapBuffer();
 *     ByteBufAllocator.DEFAULT.directBuffer();
 * }
 * </pre>
 *
 * <p> 使用池化技术可以重用ByteBuf。
 * 没有池化，每次创建新的ByteBuf实例，这个操作对于直接内存代价昂贵，对于堆内存会增加GC频率；
 * 使用池化，可以重用ByteBuf实例，并且采用jemalloc类似的内存分配算法提升分配效率。
 * 高并发场景下，池化功能更借阅内存，减少内存溢出的可能。可以通过以下参数是否开启池化功能：
 * <pre> {@code
 *     -Dio.netty.allocator.type={unpooled|pooled}
 * }
 * </pre>
 *
 */
@Slf4j
public class ByteBufDemo {
    public static void main(String[] args) {
        // 默认创建 256 bytes，使用直接内存
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        log.debug("class: {}", buffer.getClass());
        log.debug("buffer: {}", buffer);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            builder.append('k');
        }
        buffer.writeBytes(builder.toString().getBytes(StandardCharsets.UTF_8));
        log.debug("buffer: {}", buffer);
        ByteBufUtil.log(buffer);

        // (1) 堆内存
        ByteBuf heapBuffer = ByteBufAllocator.DEFAULT.heapBuffer(10);
        // (2) 直接内存
        ByteBuf directBuffer = ByteBufAllocator.DEFAULT.directBuffer(10);
    }
}
