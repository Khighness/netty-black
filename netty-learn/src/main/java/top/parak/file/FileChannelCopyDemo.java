package top.parak.file;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author KHighness
 * @since 2021-09-25
 * @apiNote FileChannel拷贝
 * <p> FileChannel只能工作在阻塞模式下
 */
@Slf4j
public class FileChannelCopyDemo {
    public static void main(String[] args) throws IOException {
        String src = "D:/Java/Learn/test/K1.txt";
        String dest = "D:/Java/Learn/test/K1.txt";
        FileInputStream in = new FileInputStream(src);
        FileChannel readChannel = in.getChannel();
        FileOutputStream out = new FileOutputStream(dest);
        FileChannel writeChannel = out.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(128);
        int bytesRead;
        while ((bytesRead = readChannel.read(buffer)) != -1) {
            log.debug("读取字节数：{}, 内容: {}", bytesRead, new String(buffer.array()));
            // 读写反转
            buffer.flip();
            writeChannel.write(buffer);
            buffer.clear();
        }
        readChannel.close();
        writeChannel.close();
        in.close();
        out.close();
    }
}
