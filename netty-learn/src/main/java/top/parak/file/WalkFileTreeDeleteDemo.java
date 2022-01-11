package top.parak.file;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author KHighness
 * @since 2021-09-28
 * @apiNote Files的walkFileTree删除目录
 */
@Slf4j
public class WalkFileTreeDeleteDemo {
    public static void main(String[] args) throws IOException {
        Files.walkFileTree(Paths.get("D:/Java/Learn/test/k"), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                log.debug("enter dir: {}", dir);
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                log.debug("delete file: {}", file);
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                log.debug("delete dir: {}", dir);
                return super.postVisitDirectory(dir, exc);
            }
        });
    }
}
