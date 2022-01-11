package top.parak.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import top.parak.message.GroupChatRequestMessage;
import top.parak.message.GroupChatResponseMessage;
import top.parak.server.session.GroupSession;
import top.parak.server.session.GroupSessionFactory;

import java.util.List;

/**
 * @author KHighness
 * @since 2021-10-04
 */
@Slf4j
@ChannelHandler.Sharable
public class GroupChatHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
        GroupSession groupSession = GroupSessionFactory.getInstance();
        List<Channel> channels = groupSession.getMemberChannels(msg.getGroupName());
        channels.forEach(channel -> {
            channel.writeAndFlush(new GroupChatResponseMessage(msg.getFrom(), msg.getContent()));
        });
    }
}
