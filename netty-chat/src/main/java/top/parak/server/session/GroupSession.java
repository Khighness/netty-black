package top.parak.server.session;

import io.netty.channel.Channel;

import java.util.List;
import java.util.Set;

/**
 * @author KHighness
 * @since 2021-10-04
 */
public interface GroupSession {

    /**
     * 创建聊天组
     *
     * @param groupName 组名称
     * @param members   成员
     * @return 创建成功返回组对象，失败返回null
     */
    Group createGroup(String groupName, Set<String> members);

    /**
     * 加入聊天组
     *
     * @param groupName 组名称
     * @param member    成员名
     * @return 组不存在则返回null，否则返回组对象
     */
    Group joinMember(String groupName, String member);

    /**
     * 移除组成员
     *
     * @param groupName 组名称
     * @param member    成员名
     * @return 组不存在则返回null，否则返回组对象
     */
    Group removeMember(String groupName, String member);

    /**
     * 解散聊天组
     *
     * @param groupName 组名称
     * @return 组不存在则返回null，否则返回组对象
     */
    Group disbandGroup(String groupName);

    /**
     * 获取组成员集合
     *
     * @param groupName 组名称
     * @return 成员名称集合，不存在则返回空集合
     */
    Set<String> getMembers(String groupName);

    /**
     * 获取组成员通道集合
     *
     * @param groupName 组名称
     * @return 成员通道集合
     */
    List<Channel> getMemberChannels(String groupName);
}
