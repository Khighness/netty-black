package top.parak.server.session;

/**
 * @author KHighness
 * @since 2021-10-04
 */
public class GroupSessionFactory {

    private static volatile GroupSession groupSession = null;

    public static GroupSession getInstance() {
        if (groupSession == null) {
            synchronized (GroupSessionFactory.class) {
                if (groupSession == null) {
                    groupSession = new GroupSessionMemoryImpl();
                }
            }
        }
        return groupSession;
    }

}
