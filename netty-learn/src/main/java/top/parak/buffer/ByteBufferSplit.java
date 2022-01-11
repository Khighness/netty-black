package top.parak.buffer;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

/**
 * @author KHighness
 * @since 2021-09-28
 * @apiNote ByteBuffer分割消息
 */
@Slf4j
public class ByteBufferSplit {
    public static void main(String[] args) {
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello, world\nI'm KHighness\nOK".getBytes());
        split(source);
        source.put("How are you\n".getBytes());
        split(source);
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
