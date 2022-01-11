package top.parak.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import top.parak.message.GroupMembersRequestMessage;
import top.parak.message.GroupMembersResponseMessage;
import top.parak.server.session.GroupSession;
import top.parak.server.session.GroupSessionFactory;

import java.util.Set;

/**
 * @author KHighness
 * @since 2021-10-05
 */
@Slf4j
@ChannelHandler.Sharable
public class GroupMembersHandler extends SimpleChannelInboundHandler<GroupMembersRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMembersRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        GroupSession groupSession = GroupSessionFactory.getInstance();
        Set<String> members = groupSession.getMembers(groupName);
        ctx.writeAndFlush(new GroupMembersResponseMessage(members));
    }
}
