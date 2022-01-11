package top.parak.serialize;

import java.io.*;

/**
 * @author KHighness
 * @since 2021-10-05
 */
public class JDKSerializer implements Serializer {

    @Override
    public <T> byte[] serialize(T obj) {
        ObjectOutputStream oos = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new SerializeException("Serialization failed");
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        ObjectInputStream ois = null;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);) {
            ois = new ObjectInputStream(bis);
            Object obj = ois.readObject();
            return clazz.cast(obj);
        } catch (IOException | ClassNotFoundException e) {
            throw new SerializeException("Deserialization failed");
        }
    }
}
