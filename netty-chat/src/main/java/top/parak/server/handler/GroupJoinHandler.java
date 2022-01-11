package top.parak.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import top.parak.message.GroupJoinRequestMessage;
import top.parak.message.GroupJoinResponseMessage;
import top.parak.server.session.Group;
import top.parak.server.session.GroupSession;
import top.parak.server.session.GroupSessionFactory;

/**
 * @author KHighness
 * @since 2021-10-04
 */
@Slf4j
@ChannelHandler.Sharable
public class GroupJoinHandler extends SimpleChannelInboundHandler<GroupJoinRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupJoinRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        String username = msg.getUsername();
        GroupSession groupSession = GroupSessionFactory.getInstance();
        Group group = groupSession.joinMember(groupName, username);
        if (group != null) {
            groupSession.getMemberChannels(groupName).forEach(channel -> {
                ctx.writeAndFlush(new GroupJoinResponseMessage(true, username + "加入群聊" + groupName));
            });
            ctx.writeAndFlush(new GroupJoinResponseMessage(true, "你已成功加入群聊" + groupName));
        } else {
            ctx.writeAndFlush(new GroupJoinResponseMessage(false, groupName + "不存在"));
        }
    }
}
