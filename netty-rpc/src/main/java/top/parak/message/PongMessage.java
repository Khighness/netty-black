package top.parak.message;

/**
 * @author KHighness
 */
public class PongMessage extends Message {
    @Override
    public int getMessageType() {
        return PongMessage;
    }
}
