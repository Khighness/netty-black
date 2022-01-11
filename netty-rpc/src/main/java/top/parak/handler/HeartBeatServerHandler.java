package top.parak.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 心跳检测处理器
 *
 * @author KHighness
 */
@Slf4j
public class HeartBeatServerHandler extends ChannelDuplexHandler {

    /**
     * IdleStatus读空闲
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            // 长时间没有读取数据，关闭通道
            if (event.state() == IdleState.READER_IDLE) {
                log.debug("长时间没有收到消息，断开连接");
                ctx.close();
            }
        }
        super.userEventTriggered(ctx,evt);
    }

}
