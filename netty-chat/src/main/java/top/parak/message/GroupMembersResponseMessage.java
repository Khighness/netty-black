package top.parak.message;

import lombok.Data;
import lombok.ToString;

import java.util.Set;

/**
 * @author KHighness
 * @since 2021-10-03
 */
@Data
@ToString(callSuper = true)
public class GroupMembersResponseMessage extends Message {

    private Set<String> members;

    public GroupMembersResponseMessage(Set<String> members) {
        this.members = members;
    }

    @Override
    public int getMessageType() {
        return GroupMembersResponseMessage;
    }
}
