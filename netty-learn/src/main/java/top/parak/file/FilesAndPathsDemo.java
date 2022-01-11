package top.parak.file;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author KHighness
 * @since 2021-09-28
 * @apiNote Files和Paths工具类拷贝
 */
@Slf4j
public class FilesAndPathsDemo {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("D:/Java/Learn/test/K1.txt");
        Path copy = Paths.get("D:/Java/Learn/test/K2.txt");
        FileSystem fileSystem = path.getFileSystem();
        log.debug("root directories: {}", fileSystem.getRootDirectories());
        log.debug("file stores: {}", fileSystem.getFileStores());
        Files.copy(path, copy);
    }
}
