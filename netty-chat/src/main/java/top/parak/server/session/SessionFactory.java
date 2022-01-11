package top.parak.server.session;

/**
 * @author KHighness
 * @since 2021-10-04
 */
public class SessionFactory {

    private static volatile Session session = null;

    public static Session getInstance() {
        if (session == null) {
            synchronized (SessionFactory.class) {
                if (session == null) {
                    session = new SessionMemoryImpl();
                }
            }
        }
        return session;
    }

}
