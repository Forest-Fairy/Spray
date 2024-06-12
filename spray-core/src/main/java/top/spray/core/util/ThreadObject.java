package top.spray.core.util;

import org.slf4j.MDC;

import java.sql.Timestamp;
import java.util.Date;

public class ThreadObject {
    private static final ThreadLocal<ThreadObject> THREAD_LOCAL = new ThreadLocal<>();
    public static final ThreadObject get() {
        ThreadObject threadObject = THREAD_LOCAL.get();
        if (threadObject == null) {
            synchronized (THREAD_LOCAL) {
                if ((threadObject = THREAD_LOCAL.get()) == null) {
                    THREAD_LOCAL.set(threadObject = new ThreadObject());
                    threadObject.init();
                }
            }
        }
        return threadObject;
    }
    private String transactionId;
    private String tid;

    public ThreadObject() {
    }

    private void init() {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        this.tid = String.valueOf(Thread.currentThread().getId());
        this.transactionId = String.valueOf(timestamp.getTime()).substring(0, 10) + timestamp.getNanos();
        MDC.put("tid", this.tid);
        MDC.put("transactionId", this.transactionId);
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getTid() {
        return tid;
    }
}
