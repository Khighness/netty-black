package top.parak.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import top.parak.message.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author KHighness
 * @since 2021-10-04
 */
@Slf4j
public class BusinessHandler extends ChannelInboundHandlerAdapter {
    private AtomicBoolean LOGIN_STATUS = new AtomicBoolean(false);
    private AtomicBoolean EXIT_STATUS = new AtomicBoolean(false);
    private CountDownLatch WAIT_FOR_LOGIN = new CountDownLatch(1);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 处理登录消息
        if (msg instanceof LoginResponseMessage) {
            LoginResponseMessage response = (LoginResponseMessage) msg;
            if (response.isSuccess()) {
                LOGIN_STATUS.set(true);
            }
            // 唤醒system-in线程
            WAIT_FOR_LOGIN.countDown();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            System.out.print("请输入用户名：");
            String username = sc.nextLine();
            System.out.print("请输入密码：");
            String password = sc.nextLine();
            LoginRequestMessage message = new LoginRequestMessage(username, password);
            ctx.writeAndFlush(message);

            try {
                WAIT_FOR_LOGIN.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!LOGIN_STATUS.get()) {
                ctx.channel().close();
                return;
            }

            while (true) {
                System.out.println("==================================");
                System.out.println("(1) send [username] [content]");
                System.out.println("(2) gsend [group name] [content]");
                System.out.println("(3) gcreate [group name] [m1,m2,m3...]");
                System.out.println("(4) gmembers [group name]");
                System.out.println("(5) gjoin [group name]");
                System.out.println("(6) gquit [group name]");
                System.out.println("(7) quit");
                System.out.println("==================================");

                String command = sc.nextLine();
                String[] s = command.split(" ");
                switch (s[0]) {
                    case "send":
                        ctx.writeAndFlush(new ChatRequestMessage(username, s[1], s[2]));
                        break;
                    case "gsend":
                        ctx.writeAndFlush(new GroupChatRequestMessage(username, s[1], s[2]));
                        break;
                    case "gcreate":
                        Set<String> set = new HashSet<>(Arrays.asList(s[2].split(",")));
                        set.add(username);
                        ctx.writeAndFlush(new GroupCreateRequestMessage(s[1], set));
                        break;
                    case "gmembers":
                        ctx.writeAndFlush(new GroupMembersRequestMessage(s[1]));
                        break;
                    case "gjoin":
                        ctx.writeAndFlush(new GroupJoinRequestMessage(username, s[1]));
                        break;
                    case "gquit":
                        ctx.writeAndFlush(new GroupQuitRequestMessage(username, s[1]));
                    case "quit":
                        ctx.channel().close();
                        return;
                    default: break;
                }
            }
        }, "system-in").start();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("连接已断开...");
        EXIT_STATUS.set(true);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.debug("产生异常：{}", cause.getMessage());
        EXIT_STATUS.set(true);
    }
}
