package top.spray.core.util;

import top.spray.core.thread.SprayThreadData;

public class SprayThreadUtil {
    public static String getThreadTransactionId() {
        return SprayThreadData.Get().getTransactionId();
    }

}
