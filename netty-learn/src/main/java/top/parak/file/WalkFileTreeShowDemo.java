package top.parak.file;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author KHighness
 * @since 2021-09-28
 * @apiNote Files的walkFileTree遍历目录
 */
@Slf4j
public class WalkFileTreeShowDemo {
    public static void main(String[] args) throws IOException {
        AtomicInteger dirCount = new AtomicInteger();
        AtomicInteger fileCount = new AtomicInteger();
        Files.walkFileTree(Paths.get("D:/Java/Learn/highness-rpc"), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                log.info("dir: {}", dir);
                dirCount.incrementAndGet();
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                log.debug("file: {}", file);
                fileCount.incrementAndGet();
                return super.visitFile(file, attrs);
            }
        });
        log.debug("dirCount = {}", dirCount.get());
        log.debug("fileCount = {}", fileCount.get());
    }
}
