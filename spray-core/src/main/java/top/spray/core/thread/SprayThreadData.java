package top.spray.core.thread;

import cn.hutool.core.util.HexUtil;
import org.slf4j.MDC;
import top.spray.core.engine.props.SprayData;
import top.spray.core.config.util.SpraySystemConfigurations;

public class SprayThreadData extends SprayData {
    private static final ThreadLocal<SprayThreadData> THREAD_LOCAL = new ThreadLocal<>();
    public static final String SEPARATOR = "(^_^)";

    public static SprayThreadData Get() {
        SprayThreadData threadData = THREAD_LOCAL.get();
        if (threadData == null) {
            synchronized (THREAD_LOCAL) {
                if ((threadData = THREAD_LOCAL.get()) == null) {
                    THREAD_LOCAL.set(threadData = new SprayThreadData());
                }
            }
        }
        return threadData;
    }

    private SprayThreadData() {
        init();
    }

    private void init() {
        super.putAll(MDC.getCopyOfContextMap());
        super.put("tid", String.valueOf(Thread.currentThread().getId()));
        super.put("transaction", HexUtil.encodeHexStr(
                SpraySystemConfigurations.macAddress() + SEPARATOR +
                        Thread.currentThread().getId() + SEPARATOR +
                        System.currentTimeMillis()) + SEPARATOR +
                        System.nanoTime());
    }

    public void setTransactionId(String transactionId) {
        super.put("transaction", transactionId);
    }

    public String getTransactionId() {
        return super.getString("transaction");
    }

    public String getTid() {
        return super.getString("tid");
    }

    public void removeLocal(SprayThreadData threadData) {
        if (this.getTransactionId().equals(threadData.getTransactionId())) {
            THREAD_LOCAL.remove();
            for (String key : this.keySet()) {
                this.remove(key);
            }
        }
    }
}
