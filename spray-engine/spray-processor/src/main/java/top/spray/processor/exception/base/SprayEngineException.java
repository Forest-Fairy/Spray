package top.spray.processor.exception.base;

import top.spray.core.exception.SprayException;

/**
 * spray.i18n.exception.engine.suffix
 */
public abstract class SprayEngineException extends SprayException {
    protected SprayEngineException(String message, Object... params) {
        super(message, params);
    }

    public SprayEngineException(Throwable cause, String message, Object... params) {
        super(cause, message, params);
    }

    public SprayEngineException(Throwable cause) {
        super(cause);
    }

    public SprayEngineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... params) {
        super(message, cause, enableSuppression, writableStackTrace, params);
    }

    @Override
    protected final String getBundleNameSuffix() {
        return "engine." + getTypeBundleNameSuffix();
    }
    protected abstract String getTypeBundleNameSuffix();
}
