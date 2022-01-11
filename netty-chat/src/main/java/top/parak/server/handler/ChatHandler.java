package top.parak.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import top.parak.message.ChatRequestMessage;
import top.parak.message.ChatResponseMessage;
import top.parak.server.session.SessionFactory;

/**
 * @author KHighness
 * @since 2021-10-04
 */
@Slf4j
@ChannelHandler.Sharable
public class ChatHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        String to = msg.getTo();
        Channel channel = SessionFactory.getInstance().getChannel(to);
        if (channel != null) { // 在线
            channel.writeAndFlush(new ChatResponseMessage(msg.getFrom(), msg.getContent()));
        } else { // 离线
            ctx.writeAndFlush(new ChatResponseMessage(false, to + "不存在或者不在线"));
        }
    }

}
