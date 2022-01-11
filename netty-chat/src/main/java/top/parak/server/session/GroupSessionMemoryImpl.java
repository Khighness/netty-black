package top.parak.server.session;

import io.netty.channel.Channel;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author KHighness
 * @since 2021-10-04
 */
public class GroupSessionMemoryImpl implements GroupSession {
    private final Map<String, Group> groupMap = new ConcurrentHashMap<>();

    @Override
    public Group createGroup(String groupName, Set<String> members) {
        Group group = new Group(groupName, members);
        return groupMap.putIfAbsent(groupName, group);
    }

    @Override
    public Group joinMember(String groupName, String member) {
        return groupMap.computeIfPresent(groupName, (key, value) -> {
            value.getMembers().add(member);
            return value;
        });
    }

    @Override
    public Group removeMember(String groupName, String member) {
        return groupMap.computeIfPresent(groupName, (key, value) -> {
            value.getMembers().remove(member);
            return value;
        });
    }

    @Override
    public Group disbandGroup(String groupName) {
        return groupMap.remove(groupName);
    }

    @Override
    public Set<String> getMembers(String groupName) {
        return groupMap.getOrDefault(groupName, Group.EMPTY_GROUP).getMembers();
    }

    @Override
    public List<Channel> getMemberChannels(String groupName) {
        return getMembers(groupName).stream()
                .map(member -> SessionFactory.getInstance().getChannel(member))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
