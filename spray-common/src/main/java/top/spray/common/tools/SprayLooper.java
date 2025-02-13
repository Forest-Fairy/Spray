package top.spray.common.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * a loop util for looping, enhance only if the exceptions will be not too many
 */
public class SprayLooper {
    public static final SprayLooper INSTANCE = new SprayLooper();

    public <T> void loop(Collection<T> collection, Consumer<T> handler) {
        this.loop(collection, handler, null);
    }

    public <T> void loop(Collection<T> collection, Consumer<T> handler, Consumer<Throwable> catcher) {
        AtomicInteger counter = new AtomicInteger();
        int size = collection.size();
        while (counter.get() < size) {
            try {
                doLoop(counter, collection, handler);
            } catch (Throwable throwable) {
                if (catcher != null) {
                    catcher.accept(throwable);
                }
            }
        }
    }

    private static <T> void doLoop(AtomicInteger start, Iterable<T> collection, Consumer<T> handler) {
        List<T> c = collection instanceof List<T> list ? list : new ArrayList<>();
        if (c.isEmpty()) {
            collection.iterator().forEachRemaining(c::add);
        }
        for (int i = start.get(); i < c.size(); i++) {
            start.incrementAndGet();
            handler.accept(c.get(i));
        }
    }
}
