package top.parak.file;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;

/**
 * @author KHighness
 * @since 2021-09-28
 * @apiNote Files的walk拷贝目录
 * <p> 注意路径要换成\\，不能用/
 */
@Slf4j
public class WalkCopyDemo {
    public static void main(String[] args) throws IOException {
        String src = "D:\\Java\\Learn\\highness-rpc\\highness-rpc-core";
        String dest = "D:\\Java/Learn\\test\\highness-rpc-core-copy";
        Files.walk(Paths.get(src)).forEach(path -> {
            String target = path.toString().replace(src, dest);
            log.info("path: {}, target: {}", path, target);
            try {
                if (Files.isDirectory(path)) {
                    Files.createDirectory(Paths.get(target));
                } else if (Files.isRegularFile(path)) {
                    Files.copy(path, Paths.get(target));
                }
            } catch (IOException e) {
                log.error("occur exception while copying: {}", e.getMessage(), e);
            }
        });
    }
}
