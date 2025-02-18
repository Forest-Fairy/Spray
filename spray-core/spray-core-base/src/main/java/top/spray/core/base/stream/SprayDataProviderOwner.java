package top.spray.core.base.stream;

import top.spray.core.global.prop.SprayData;

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
