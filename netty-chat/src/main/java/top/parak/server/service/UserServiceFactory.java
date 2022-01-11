package top.parak.server.service;

/**
 * @author KHighness
 * @since 2021-10-04
 */
public class UserServiceFactory {

    private static volatile UserService userService = null;

    public static UserService getInstance() {
        if (userService == null) {
            synchronized (UserService.class) {
                if (userService == null) {
                    userService = new UserServiceImpl();
                }
            }
        }
        return userService;
    }

}
