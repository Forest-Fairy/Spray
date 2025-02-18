package top.spray.core.stream;

import top.spray.core.global.prop.SprayData;

import java.util.Collection;

public interface SprayDataProvider extends Iterable<SprayData> {
    int curSize();
    boolean addableFor(int willAddSize);
    void add(SprayData data);
    void addAll(Collection<SprayData> all);
    void addAll(SprayDataProvider all);
}
