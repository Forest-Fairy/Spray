package top.spray.core.global.thread;

public class SprayThreadUtil {
    public static String getThreadTransactionId() {
        return SprayThreadData.Get().getTransactionId();
    }

}
