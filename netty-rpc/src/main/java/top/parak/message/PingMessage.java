package top.parak.message;

/**
 * @author KHighness
 */
public class PingMessage extends Message {
    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
