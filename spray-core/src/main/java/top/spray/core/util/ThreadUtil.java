package top.spray.core.util;

public class ThreadUtil {
    public static String getThreadTransactionId() {
        return SprayThreadData.Get().getTransactionId();
    }

}
