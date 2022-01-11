package top.parak.serialize;

/**
 * @author KHighness
 * @since 2021-10-05
 */
public class SerializerFactory {
    public static final int JDK_SERIALIZATION = 1;
    public static final int KRYO_SERIALIZATION = 2;
    public static final int PROTOSTUFF_SERIALIZATION = 3;
    public static final int JSON_SERIALIZATION = 4;

    private static final JDKSerializer jdkSerializer = new JDKSerializer();
    private static final KryoSerializer kryoSerializer = new KryoSerializer();
    private static final ProtostuffSerializer protostuffSerializer = new ProtostuffSerializer();
    private static final JsonSerializer jsonSerializer = new JsonSerializer();

    public static Serializer getInstance(int serializationType) {
        switch (serializationType) {
            case JDK_SERIALIZATION:
                return jdkSerializer;
            case KRYO_SERIALIZATION:
                return kryoSerializer;
            case PROTOSTUFF_SERIALIZATION:
                return protostuffSerializer;
            case JSON_SERIALIZATION:
                return jsonSerializer;
            default:
                throw new IllegalArgumentException("serialization type");
        }
    }
}
