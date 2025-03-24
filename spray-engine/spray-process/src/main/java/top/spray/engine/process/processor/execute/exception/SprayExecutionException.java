package top.spray.engine.process.processor.execute.exception;

import top.spray.engine.exception.SprayEngineException;

/**
 * spray.i18n.exception.engine.execute
 */
public class SprayExecutionException extends SprayEngineException {

    protected SprayExecutionException(String messageKey, Object... params) {
        super(messageKey, params);
    }

    protected SprayExecutionException(Throwable cause, String messageKey, Object... params) {
        super(cause, messageKey, params);
    }

    protected SprayExecutionException(Throwable cause) {
        super(cause);
    }

    protected SprayExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... params) {
        super(message, cause, enableSuppression, writableStackTrace, params);
    }

    @Override
    protected String getTypeBundleNameSuffix() {
        return "execute";
    }
}
