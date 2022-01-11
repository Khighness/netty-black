package top.parak.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import top.parak.message.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author KHighness
 * @since 2021-10-05
 */
public class KryoSerializer implements Serializer {

    /**
     * Because Kryo is not thread safe. So, use ThreadLocal to store Kryo objects
     */
    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(PingMessage.class);
        kryo.register(PongMessage.class);
        kryo.register(LoginRequestMessage.class);
        kryo.register(LoginResponseMessage.class);
        kryo.register(ChatRequestMessage.class);
        kryo.register(ChatResponseMessage.class);
        kryo.register(GroupChatRequestMessage.class);
        kryo.register(GroupChatResponseMessage.class);
        kryo.register(GroupCreateRequestMessage.class);
        kryo.register(GroupChatResponseMessage.class);
        kryo.register(GroupJoinRequestMessage.class);
        kryo.register(GroupJoinResponseMessage.class);
        kryo.register(GroupMembersRequestMessage.class);
        kryo.register(GroupMembersResponseMessage.class);
        kryo.register(GroupQuitRequestMessage.class);
        kryo.register(GroupQuitResponseMessage.class);
        return kryo;
    });

    @Override
    public <T> byte[] serialize(T obj) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             Output output = new Output(bos)) {
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (IOException e) {
            throw new SerializeException("Serialization failed");
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream byteArrayOutputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayOutputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            return kryo.readObject(input, clazz);
        } catch (IOException e) {
            throw new SerializeException("Deserialization failed");
        }
    }
}
