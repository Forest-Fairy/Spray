package top.spray.core.thread;

public class SprayThreadUtil {
    public static String getThreadTransactionId() {
        return SprayThreadData.Get().getTransactionId();
    }

}
