package top.parak.manager;

import com.alibaba.nacos.api.exception.NacosException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;
import top.parak.handler.HeartBeatClientHandler;
import top.parak.handler.RpcResponseMessageHandler;
import top.parak.loadbalancer.RoundRobinRule;
import top.parak.message.RpcRequestMessage;
import top.parak.protocol.MessageCodecSharable;
import top.parak.protocol.ProcotolFrameDecoder;
import top.parak.registry.NacosServerDiscovery;
import top.parak.registry.ServerDiscovery;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 管理Rpc客户端的channel
 *
 * @author KHighness
 */
@Slf4j
public class RpcClientManager {

    /**
     * 启动器
     */
    private static final Bootstrap bootstrap;

    /**
     *  事件循环组
     */
    static NioEventLoopGroup group;

    /**
     * 服务发现
     */
    private final ServerDiscovery serviceDiscovery;

    /**
     * key: InetSocketAddress, value: Channel
     */
    private static Map<String, Channel> channels;

    /**
     * key: sequenceId，value: Promise
     */
    public static final Map<Integer, Promise<Object>> PROMISES;

    static {
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup();
        initChannel();
        channels = new ConcurrentHashMap<>();
        PROMISES = new ConcurrentHashMap<>();
    }

    public RpcClientManager() {
        this.serviceDiscovery = new NacosServerDiscovery(new RoundRobinRule());
    }

    private static Bootstrap initChannel() {
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable messageCodec = new MessageCodecSharable();
        RpcResponseMessageHandler rpcResponseMessageHandler = new RpcResponseMessageHandler();
        HeartBeatClientHandler heartBeatClientHandler = new HeartBeatClientHandler();
        bootstrap.channel(NioSocketChannel.class)
                .group(group)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new ProcotolFrameDecoder());
                        p.addLast(messageCodec);
                        p.addLast(loggingHandler);
                        p.addLast(heartBeatClientHandler);
                        p.addLast(rpcResponseMessageHandler);
                    }
                });
        return bootstrap;
    }

    /**
     * 获取通道
     * @param inetSocketAddress 网络地址
     * @return 通道
     */
    public static Channel get(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        if (channels.containsKey(key)) {
            Channel channel = channels.get(key);
            if (channel != null && channel.isActive()) {
                return channel;
            }
            channels.remove(key);
        }
        Channel channel = null;
        try {
            channel = bootstrap.connect(inetSocketAddress).sync().channel();
            channel.closeFuture().addListener(future -> log.debug("断开连接"));
        } catch (InterruptedException e) {
            channel.close();
            log.debug("客户端连接失败：{}" + e.getMessage());
            return null;
        }
        channels.put(key, channel);
        return channel;
    }

    public void sendRpcRequest(RpcRequestMessage msg) throws NacosException {
        InetSocketAddress service = serviceDiscovery.getService(msg.getInterfaceName());
        Channel channel = get(service);
        if (channel != null && (!channel.isActive() || !channel.isRegistered())) {
            group.shutdownGracefully();
            return;
        }
        channel.writeAndFlush(msg).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.debug("客户端发送消息成功");
            }
        });
    }
}
