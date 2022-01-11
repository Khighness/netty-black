package top.parak.server.service;

/**
 * @author KHighness
 * @since 2021-10-04
 */
public interface UserService {

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 成功返回 true，失败返回 false
     */
    boolean login(String username, String password);
}
