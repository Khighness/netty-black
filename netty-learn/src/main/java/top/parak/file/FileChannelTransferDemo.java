package top.parak.file;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
/**
 * @author KHighness
 * @since 2021-09-26
 * @apiNote FileChannel的transfer拷贝
 */
@Slf4j
public class FileChannelTransferDemo {
    // transfer方法最多只能传输Integer.MAX_VALUE
    public static void main(String[] args) throws IOException {
        String src = "D:\\BaiduNetdiskDownload\\复联4\\复仇者联盟4-终局之战.mp4";
        String dest = "D:\\BaiduNetdiskDownload\\复联4\\复仇者联盟4-终局之战-copy.mp4";
        FileChannel from = new FileInputStream(src).getChannel();
        FileChannel to = new FileOutputStream(dest).getChannel();
        long size = from.size();
        log.debug("开始拷贝...");
        for (long i = size; i > 0;) {
            long copy = from.transferTo((size - i), i, to);
            log.debug("position: {}, copy size: {}", (size - i), copy);
            i -= copy;
        }
        log.debug("拷贝完毕");
    }
}
