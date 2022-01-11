package top.parak.buffer;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author KHighness
 * @since 2021-09-28
 * @apiNote ByteBuffer与String的想换转换
 */
@Slf4j
public class ByteBufferString {
    public static void main(String[] args) {
        ByteBuffer buffer1 = StandardCharsets.UTF_8.encode("Hello, KHighness");
        ByteBuffer buffer2 = Charset.forName("utf-8").encode("Hello, KHighness");
        ByteBuffer buffer3 = ByteBuffer.wrap("Hello, KHighness".getBytes());
        ByteBufferUtil.debugAll(buffer1);
        ByteBufferUtil.debugAll(buffer2);
        ByteBufferUtil.debugAll(buffer3);
        CharBuffer buffer4 = StandardCharsets.UTF_8.decode(buffer1);
        log.debug("{}", buffer4.getClass());
        log.debug("{}", buffer4.toString());
    }
}
