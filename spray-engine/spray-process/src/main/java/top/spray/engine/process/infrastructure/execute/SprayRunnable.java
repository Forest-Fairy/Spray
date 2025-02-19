package top.spray.engine.process.infrastructure.execute;


import java.util.concurrent.Callable;

public interface SprayRunnable<T> extends Runnable, Callable<T> {
    @Override
    default void run() {
        this.start();
    }


    /** a method for completable future */
    @Override
    default T call() throws Exception {
        this.start();
        return this.runningStatus();
    }

    boolean canDoStart();
    void start();

    boolean canDoResume();
    void resume();

    boolean canDoPause();
    void pause();

    boolean canDoCancel();
    void cancel();

    T runningStatus();

}
