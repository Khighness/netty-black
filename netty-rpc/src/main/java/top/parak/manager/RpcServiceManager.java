package top.parak.manager;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import top.parak.annotation.server.RpcServer;
import top.parak.annotation.server.RpcServerScan;
import top.parak.factory.ServiceFactory;
import top.parak.handler.HeartBeatServerHandler;
import top.parak.handler.PingMessageHandler;
import top.parak.handler.RpcRequestMessageHandler;
import top.parak.protocol.MessageCodecSharable;
import top.parak.protocol.ProcotolFrameDecoder;
import top.parak.registry.NacosServerRegistry;
import top.parak.registry.ServerRegistry;
import top.parak.utils.PackageScanUtils;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Rpc服务端管理器
 *
 * @author KHighness
 */
@Slf4j
public class RpcServiceManager {
    public static final int DEFAULT_SERVER_PORT = 3333;

    protected String host;
    protected int port;
    protected ServerRegistry serverRegistry;
    protected ServiceFactory serviceFactory;
    NioEventLoopGroup worker = new NioEventLoopGroup();
    NioEventLoopGroup boss = new NioEventLoopGroup();
    ServerBootstrap bootstrap = new ServerBootstrap();

    public RpcServiceManager(String host, int port) {
        this.host = host;
        this.port = port;
        serverRegistry = new NacosServerRegistry();
        serviceFactory = new ServiceFactory();
        autoRegistry();
    }

    public void start() {
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable messageCodec = new MessageCodecSharable();
        HeartBeatServerHandler heartBeatServerHandler = new HeartBeatServerHandler();
        PingMessageHandler pingMessageHandler = new PingMessageHandler();
        RpcRequestMessageHandler rpcRequestMessageHandler = new RpcRequestMessageHandler();

        try {
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                            pipeline.addLast(new ProcotolFrameDecoder());//定长解码器
                            pipeline.addLast(messageCodec);
                            pipeline.addLast(loggingHandler);
                            // pipeline.addLast(heartBeatServerHandler);
                            // pipeline.addLast(pingMessageHandler);
                            pipeline.addLast(rpcRequestMessageHandler);
                        }
                    });
            Channel channel = bootstrap.bind(port).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("启动服务出错");
        }finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }

    /**
     * 扫描@RpcServer注解，注册服务
     */
    public void autoRegistry() {
        String mainClassPath = PackageScanUtils.getStackTrace();
        Class<?> mainClass;
        try {
            mainClass = Class.forName(mainClassPath);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("启动类未找到");
        }
        if (mainClass.isAnnotationPresent(RpcServer.class)) {
            throw new RuntimeException("启动类缺少@RpcServer注解");
        }
        String annotationValue = mainClass.getAnnotation(RpcServerScan.class).value();
        // 如果注解路径的值是空，则等于main父路径包下
        if ("".equals(annotationValue)) {
            annotationValue = mainClassPath.substring(0, mainClassPath.lastIndexOf("."));
        }
        // 获取所有类的set集合
        Set<Class<?>> set = PackageScanUtils.getClasses(annotationValue);
        for (Class<?> c : set) {
            // 只有有@RpcServer注解的才注册
            if (c.isAnnotationPresent(RpcServer.class)) {
                String ServerNameValue = c.getAnnotation(RpcServer.class).name();
                Object object;
                try {
                    object = c.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error("创建对象发生错误：{}", c);
                    continue;
                }
                // 注解的值如果为空，使用类名
                if ("".equals(ServerNameValue)) {
                    addServer(object, c.getCanonicalName());
                } else {
                    addServer(object, ServerNameValue);
                }
            }
        }
    }

    /**
     * 添加对象到工厂和注册到注册中心
     */
    public <T> void addServer(T server, String serverName) {
        serviceFactory.addServiceProvider(server, serverName);
        serverRegistry.register(serverName, new InetSocketAddress(host, port));
    }

}
