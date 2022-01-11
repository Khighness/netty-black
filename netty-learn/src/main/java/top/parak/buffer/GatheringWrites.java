package top.parak.buffer;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author KHighness
 * @since 2021-09-28
 * @apiNote 集中写入
 */
@Slf4j
public class GatheringWrites {
    public static void main(String[] args) {
        ByteBuffer b1 = StandardCharsets.UTF_8.encode("Hello,");
        ByteBuffer b2 = StandardCharsets.UTF_8.encode("KHighness");
        ByteBuffer b3 = StandardCharsets.UTF_8.encode("!");
        try (FileChannel fileChannel = new RandomAccessFile("D:/Java/Learn/test/w.txt", "rw").getChannel()) {
            fileChannel.write(new ByteBuffer[] {b1, b2, b3});
        } catch (IOException e) {
            log.error("occur exception: {}", e.getMessage());
        }
    }
}
