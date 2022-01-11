package top.parak.serialize;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;

/**
 * @author KHighness
 * @since 2021-10-05
 */
public class JsonSerializer implements Serializer{

    @Override
    public <T> byte[] serialize(T obj) {
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        return json.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Gson gson = new Gson();
        String json = new String(bytes, StandardCharsets.UTF_8);
        return gson.fromJson(json, clazz);
    }
}
