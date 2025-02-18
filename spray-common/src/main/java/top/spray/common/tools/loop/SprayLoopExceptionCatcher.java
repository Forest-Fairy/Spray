package top.spray.common.tools.loop;

@FunctionalInterface
public interface SprayLoopExceptionCatcher {
    void accept(Throwable t);
}