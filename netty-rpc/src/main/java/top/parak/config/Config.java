package top.parak.config;

import top.parak.manager.RpcServiceManager;
import top.parak.serialize.SerializerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置
 *
 * @author KHighness
 */
public abstract class Config {
    static Properties properties;
    static {
        try (InputStream in = Config.class.getResourceAsStream("/rpc.properties")) {
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static int getServerPort() {
        String value = properties.getProperty("server.port");
        if(value == null) {
            return RpcServiceManager.DEFAULT_SERVER_PORT;
        } else {
            return Integer.parseInt(value);
        }
    }

    public static int getSerializerType() {
        String type = properties.getProperty("serialize.type");
        if(type == null) {
            return SerializerFactory.DEFAULT_SERIALIZATION;
        } else {
            return Integer.parseInt(type);
        }
    }
}
