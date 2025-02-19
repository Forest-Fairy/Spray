package top.spray.engine.process.processor.dispatch.exception;

import top.spray.engine.exception.SprayEngineException;

/**
 * spray.i18n.exception.engine.coordinate
 */
public abstract class SprayCoordinateException extends SprayEngineException {

    protected SprayCoordinateException(String messageKey, Object... params) {
        super(messageKey, params);
    }

    protected SprayCoordinateException(Throwable cause, String messageKey, Object... params) {
        super(cause, messageKey, params);
    }

    protected SprayCoordinateException(Throwable cause) {
        super(cause);
    }

    protected SprayCoordinateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... params) {
        super(message, cause, enableSuppression, writableStackTrace, params);
    }

    @Override
    protected final String getTypeBundleNameSuffix() {
        return "coordinate";
    }

}
