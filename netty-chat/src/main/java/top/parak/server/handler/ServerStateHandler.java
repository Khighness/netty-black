package top.parak.server.handler;

import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author KHighness
 * @since 2021-10-05
 */
public class ServerStateHandler extends IdleStateHandler {

    public ServerStateHandler() {
        this(5, 0, 0);
    }

    public ServerStateHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds, int allIdleTimeSeconds) {
        super(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds);
    }
}
