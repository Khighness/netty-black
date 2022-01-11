package top.parak.server.session;

import io.netty.channel.Channel;

/**
 * @author KHighness
 * @since 2021-10-04
 */
public interface Session {

    /**
     * 绑定会话
     *
     * @param channel  通道
     * @param username 用户名
     */
    void bind(Channel channel, String username);

    /**
     * 解绑会话
     *
     * @param channel 通道
     */
    void unbind(Channel channel);

    /**
     * 获取属性
     *
     * @param channel  通道
     * @param username 用户名
     * @return 属性值
     */
    Object getAttribute(Channel channel, String username);

    /**
     * 根据用户名获取通道
     *
     * @param username 用户名
     * @return 通道
     */
    Channel getChannel(String username);

}
