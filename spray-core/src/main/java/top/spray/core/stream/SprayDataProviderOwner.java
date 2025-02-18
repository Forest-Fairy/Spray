package top.spray.core.stream;

import top.spray.common.data.SprayData;

import java.util.Collection;
import java.util.List;

public abstract class SprayDataProviderOwner {
    SprayDataProviderOwner() {
    }

    public abstract SprayDataStream push(SprayData data) ;

    public abstract SprayDataStream pushMany(Collection<SprayData> many) ;

    public abstract SprayDataStream pushMany(SprayDataProvider provider) ;

    public abstract List<SprayDataProvider> providers();

    public abstract List<Long> providerRanges();
}
