package top.parak.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import top.parak.message.GroupChatResponseMessage;
import top.parak.message.GroupCreateRequestMessage;
import top.parak.message.GroupCreateResponseMessage;
import top.parak.server.session.Group;
import top.parak.server.session.GroupSession;
import top.parak.server.session.GroupSessionFactory;

import java.util.List;
import java.util.Set;

/**
 * @author KHighness
 * @since 2021-10-04
 */
@Slf4j
@ChannelHandler.Sharable
public class GroupCreateHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        Set<String> members = msg.getMembers();
        GroupSession groupSession = GroupSessionFactory.getInstance();
        Group group = groupSession.createGroup(groupName, members);
            if (group == null) {
                // 发送拉群消息
                List<Channel> channels = groupSession.getMemberChannels(groupName);
                channels.forEach(channel -> { channel.writeAndFlush(new GroupCreateResponseMessage(true, "你已被拉入群聊" + groupName)); });
                // 发送成功消息
                ctx.writeAndFlush(new GroupChatResponseMessage(true, groupName + "创建成功"));
        } else {
            ctx.writeAndFlush(new GroupChatResponseMessage(false, groupName + "创建失败"));
        }
    }
}
