package top.spray.engine.util;

public class ThreadUtil {
    public static String getThreadTransactionId() {
        return ThreadObject.get().getTransactionId();
    }

}
