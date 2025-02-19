package top.spray.common.tools.loop;

import java.util.*;

/**
 * a loop util for looping, enhance only if the exceptions will be not too many
 */
public class SprayLooper {
    private SprayLooper() {}

    @SuppressWarnings("rawtypes")
    private static final SprayLoopExceptionCatcher IGNORE_EXCEPTION_CATCHER = (t, ignored) -> {};
    public static <T> void loopAndIgnoredException(Iterable<T> collection, SprayLoopHandler<T> handler) {
        // noinspection unchecked
        loop(collection, handler, IGNORE_EXCEPTION_CATCHER);
    }

    public static <T> void loop(Iterable<T> collection, SprayLoopHandler<T> handler, SprayLoopExceptionCatcher<T> catcher) {
        Iterator<T> it = collection.iterator();
        while (true) {
            if (!it.hasNext()) {
                return;
            }
            T next = null;
            try {
                while (it.hasNext()) {
                    next = it.next();
                    handler.accept(it.hasNext(), next);
                }
            } catch (Throwable throwable) {
                if (catcher != null) {
                    catcher.accept(next, throwable);
                } else {
                    throw new RuntimeException(throwable);
                }
            }
        }
    }

}
