package top.spray.core.base.thread;

public class SprayThreadUtil {
    public static String getThreadTransactionId() {
        return SprayThreadData.Get().getTransactionId();
    }

}
