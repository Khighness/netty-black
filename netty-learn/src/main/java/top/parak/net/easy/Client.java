package top.parak.net.easy;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author KHighness
 * @since 2021-09-28
 */
@Slf4j
public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress(BlockingServer.port));
        sc.write(StandardCharsets.UTF_8.encode("Hello, KHighness!\n"));
        System.in.read();
    }
}
