package top.parak.buffer;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author KHighness
 * @since 2021-09-28
 * @apiNote 分散读取
 */
@Slf4j
public class ScatteringReads {
    public static void main(String[] args) {
        try (FileChannel fileChannel = new RandomAccessFile("D:/Java/Learn/test/K.txt", "r").getChannel()) {
            ByteBuffer b1 = ByteBuffer.allocate(3);
            ByteBuffer b2 = ByteBuffer.allocate(3);
            ByteBuffer b3 = ByteBuffer.allocate(3);
            fileChannel.read(new ByteBuffer[] {b1, b2, b3});
            log.debug("b1 = {}", new String(b1.array()));
            log.debug("b2 = {}", new String(b2.array()));
            log.debug("b3 = {}", new String(b3.array()));
        } catch (IOException e) {
            log.error("occur exception: {}", e.getMessage());
        }
    }
}
