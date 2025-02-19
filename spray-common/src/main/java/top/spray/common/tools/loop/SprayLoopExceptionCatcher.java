package top.spray.common.tools.loop;

@FunctionalInterface
public interface SprayLoopExceptionCatcher<T> {
    void accept(T next, Throwable t);
}