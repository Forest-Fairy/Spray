package top.spray.core.global.prop;

import java.util.function.Function;
import java.util.function.Supplier;

public interface SprayUnsupportedOperation {
    static <T> T unsupported() {
        return unsupported(Thread.currentThread().getStackTrace()[1]);
    }
    static <T> T unsupported(StackTraceElement traceElement) {
        return unsupported((String) null);
    }
    static <T> T unsupported(String msg) {
        return unsupported(o -> o, msg);
    }
    static <T> T unsupported(Function<UnsupportedOperationException, ? extends RuntimeException> errorWrapper, String msg) {
        throw msg == null
                ? errorWrapper.apply(new UnsupportedOperationException())
                : errorWrapper.apply(new UnsupportedOperationException(msg));
    }
}
