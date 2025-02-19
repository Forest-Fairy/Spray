package top.spray.engine.process.infrastructure.execute;


import java.util.concurrent.Callable;

public interface SprayRunnable<T> extends Runnable, Callable<T> {
    @Override
    default void run() {
        this.startOrContinue();
    }


    /** a method for completable future */
    @Override
    default T call() throws Exception {
        this.startOrContinue();
        return this.runningStatus();
    }

    void startOrContinue();

    void pause();

    void cancel();

    T runningStatus();

}
