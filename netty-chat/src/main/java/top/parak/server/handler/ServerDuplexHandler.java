package top.parak.server.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author KHighness
 * @since 2021-10-05
 */
@Slf4j
public class ServerDuplexHandler extends ChannelDuplexHandler {

    /**
     * 触发特殊事件
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;
        if (event.state() == IdleState.READER_IDLE) {
            log.debug("Client [{}] has not sent data for 5 seconds", ctx.channel().remoteAddress().toString().substring(1));
        }
    }
}
