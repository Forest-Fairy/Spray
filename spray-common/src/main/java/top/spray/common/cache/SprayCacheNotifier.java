package top.spray.common.cache;

public interface SprayCacheNotifier {
    void notify(String key, Object value, int status);
}
