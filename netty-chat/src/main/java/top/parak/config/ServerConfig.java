package top.parak.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author KHighness
 * @since 2021-10-05
 */
public abstract class ServerConfig {

    private static Properties properties;

    static {
        try (InputStream in = ServerConfig.class.getResourceAsStream("/server.properties")) {
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static int getServerPort() {
        String port = properties.getProperty("server.port");
        if (port == null) {
            return 3333;
        } else {
            return Integer.parseInt(port);
        }
    }

    public static int getSerializationType() {
        String type = properties.getProperty("serialize.type");
        if (type == null) {
            return 1;
        } else {
            return Integer.parseInt(type);
        }
    }
}
