package top.parak.handler;


import top.parak.factory.ServiceFactory;
import top.parak.message.RpcRequestMessage;
import top.parak.message.RpcResponseMessage;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Rpc请求处理器
 *
 * @author KHighness
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {

    /**
     * 读事件
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage message) {
        RpcResponseMessage responseMessage=new RpcResponseMessage();
        // 设置请求的序号
        responseMessage.setSequenceId(message.getSequenceId());
        Object result;
        try {
            // 通过名称从工厂获取本地注解了@RpcServer的实例
            Object service = ServiceFactory.serviceFactory.get(message.getInterfaceName());
            // 通过方法名，参数获取方法
            Method method = service.getClass().getMethod(message.getMethodName(),message.getParameterTypes());
            // 反射调用
            result = method.invoke(service, message.getParameterValue());
            // 设置返回值
            responseMessage.setReturnValue(result);
        } catch ( NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            responseMessage.setExceptionValue(new Exception("远程调用出错: " + e.getMessage()));
        } finally {
            ctx.writeAndFlush(responseMessage);
            ReferenceCountUtil.release(message);
        }
    }

    /**
     * 读空闲
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.info("长时间未收到心跳包，断开连接...");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
