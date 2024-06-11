package top.spray.engine.step.executor.closeable;

import top.spray.engine.step.executor.SprayProcessStepExecutor;


public interface SprayCloseableExecutor extends SprayProcessStepExecutor {
    void close();
}
