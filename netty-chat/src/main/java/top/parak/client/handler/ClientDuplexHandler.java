package top.parak.client.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import top.parak.message.PingMessage;

/**
 * @author KHighness
 * @since 2021-10-05
 */
@Slf4j
public class ClientDuplexHandler extends ChannelDuplexHandler {

    /**
     * 触发特殊事件
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;
        if (event.state() == IdleState.WRITER_IDLE) {
            ctx.writeAndFlush(new PingMessage());
            log.debug("Send heartbeat packets every 3 seconds");
        }
    }
}
