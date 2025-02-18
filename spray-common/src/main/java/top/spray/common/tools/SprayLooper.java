package top.spray.common.tools;

import java.util.*;

/**
 * a loop util for looping, enhance only if the exceptions will be not too many
 */
public class SprayLooper {
    private SprayLooper() {}


    public static <T> void loop(Iterable<T> collection, Handler<T> handler) {
        loop(collection, handler, ignoredEx -> {});
    }

    public static <T> void loop(Iterable<T> collection, Handler<T> handler, Catcher catcher) {
        Iterator<T> it = collection.iterator();
        while (true) {
            if (!it.hasNext()) {
                return;
            }
            try {
                while (it.hasNext()) {
                    handler.accept(it.next());
                }
            } catch (Throwable throwable) {
                if (catcher != null) {
                    catcher.accept(throwable);
                } else {
                    throw new RuntimeException(throwable);
                }
            }
        }
    }

    @FunctionalInterface
    interface Handler<T> {
        void accept(T t) throws Exception;
    }

    @FunctionalInterface
    interface Catcher {
        void accept(Throwable t);
    }
}
