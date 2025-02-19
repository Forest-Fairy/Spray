package top.spray.common.tools.loop;

@FunctionalInterface
public interface SprayLoopHandler<T> {
    void accept(boolean hasNext, T t) throws Exception;
}