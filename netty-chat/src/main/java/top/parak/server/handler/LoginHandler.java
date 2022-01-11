package top.parak.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import top.parak.message.LoginRequestMessage;
import top.parak.message.LoginResponseMessage;
import top.parak.server.service.UserServiceFactory;
import top.parak.server.session.SessionFactory;

/**
 * @author KHighness
 * @since 2021-10-04
 */
@Slf4j
@ChannelHandler.Sharable
public class LoginHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        LoginResponseMessage message;
        String username = msg.getUsername();
        if (UserServiceFactory.getInstance().login(username, msg.getPassword())) {
            SessionFactory.getInstance().bind(ctx.channel(), msg.getUsername());
            message = new LoginResponseMessage(true, "登录成功");
            log.info("User login successfully: [{}]", username);
        } else {
            message = new LoginResponseMessage(false, "用户名或密码不正确");
            log.info("User login failed: [{}]", username);
        }
        ctx.writeAndFlush(message);
    }
}
