package top.parak.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import top.parak.message.GroupQuitRequestMessage;
import top.parak.message.GroupQuitResponseMessage;
import top.parak.server.session.Group;
import top.parak.server.session.GroupSession;
import top.parak.server.session.GroupSessionFactory;

/**
 * @author KHighness
 * @since 2021-10-05
 */
@Slf4j
@ChannelHandler.Sharable
public class GroupQuitHandler extends SimpleChannelInboundHandler<GroupQuitRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupQuitRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        String username = msg.getUsername();
        GroupSession groupSession = GroupSessionFactory.getInstance();
        Group group = groupSession.removeMember(groupName, username);
        if (group != null) {
            groupSession.getMemberChannels(groupName).forEach(channel -> {
                ctx.writeAndFlush(new GroupQuitResponseMessage(true, username + "退出群聊" + groupName));
            });
            ctx.writeAndFlush(new GroupQuitResponseMessage(true, "退群成功"));
        } else {
            ctx.writeAndFlush(new GroupQuitResponseMessage(false, "该群不存在"));
        }
    }
}
