package top.spray.core.base.stream;

import top.spray.common.tools.SprayOptional;
import top.spray.core.global.prop.SprayData;

import java.util.Map;
import java.util.function.Function;

public class SprayStreams {
    public static final SprayStreams INSTANCE = new SprayStreams();
    private SprayStreams() {}
    public static <T> SprayOptional<T> optionalOf(T t) {
        return new SprayOptional<>(t);
    }

    public static SprayDataStream streamOf(SprayDataProvider provider, Map<String, String> props) {
        return new SprayDataStream(provider, props);
    }

    public static <T> SprayDataStream streamOf(Iterable<T> data, Function<T, SprayData> castor, Map<String, String> props) {
        SprayDataStream.DefaultDataProvider provider = SprayDataStream.newDataProvider();
        data.forEach(dt -> provider.add(castor.apply(dt)));
        return new SprayDataStream(provider, props);
    }


}
