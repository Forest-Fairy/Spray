package top.spray.common.tools.loop;

import java.util.*;

/**
 * a loop util for looping, enhance only if the exceptions will be not too many
 */
public class SprayLooper {
    private SprayLooper() {}

    public static <T> void loopAndIgnoredException(Iterable<T> collection, SprayLoopHandler<T> handler) {
        loop(collection, handler, ignored -> {});
    }

    public static <T> void loop(Iterable<T> collection, SprayLoopHandler<T> handler, SprayLoopExceptionCatcher catcher) {
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

}
