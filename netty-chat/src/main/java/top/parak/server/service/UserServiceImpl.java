package top.parak.server.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author KHighness
 * @since 2021-10-04
 */
public class UserServiceImpl implements UserService {
    private final Map<String, String> users = new ConcurrentHashMap<>();

    {
        users.put("K1", "1");
        users.put("K2", "2");
        users.put("K3", "3");
        for (int i = 1; i <= 10; i++) {
            users.put("K" + i, "" + i);
        }
    }

    @Override
    public boolean login(String username, String password) {
        String dbPass = users.get(username);
        return dbPass != null && dbPass.equals(password);
    }
}
