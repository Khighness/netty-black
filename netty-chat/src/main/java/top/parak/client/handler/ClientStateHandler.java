package top.parak.client.handler;

import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author KHighness
 * @since 2021-10-05
 */
public class ClientStateHandler extends IdleStateHandler {

    public ClientStateHandler() {
        this(0, 3, 0);
    }

    public ClientStateHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds, int allIdleTimeSeconds) {
        super(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds);
    }
}
