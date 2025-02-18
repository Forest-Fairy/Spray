package top.spray.common.tools.loop;

@FunctionalInterface
public interface SprayLoopHandler<T> {
    void accept(T t) throws Exception;
}