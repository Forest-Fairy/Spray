package top.spray.engine.process.processor.data.exception;

/**
 * spray.i18n.exception.engine.data
 */
public class SprayDataException extends SprayEngineException {

    protected SprayDataException(String messageKey, Object... params) {
        super(messageKey, params);
    }

    protected SprayDataException(Throwable cause, String messageKey, Object... params) {
        super(cause, messageKey, params);
    }

    protected SprayDataException(Throwable cause) {
        super(cause);
    }

    protected SprayDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... params) {
        super(message, cause, enableSuppression, writableStackTrace, params);
    }

    @Override
    protected final String getTypeBundleNameSuffix() {
        return "data";
    }

}
