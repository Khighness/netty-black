package top.parak.message;

/**
 * @author KHighness
 * @since 2021-10-03
 */
public class PingMessage extends Message {
    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
