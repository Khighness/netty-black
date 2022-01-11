package top.parak.message;

/**
 * @author KHighness
 * @since 2021-10-03
 */
public class PongMessage extends Message {
    @Override
    public int getMessageType() {
        return PongMessage;
    }
}
