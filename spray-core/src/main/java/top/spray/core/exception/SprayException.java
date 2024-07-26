package top.spray.core.exception;

import top.spray.core.i18n.message.SprayMessageObject;

/**
 * define resource bundle with prefix
 * <h4>spray.i18n.exception</h4>
 */
public abstract class SprayException extends RuntimeException implements SprayMessageObject {
    private static final String PREFIX = BUNDLE_PREFIX + "exception.";

    protected SprayException() {
    }

    protected SprayException(String message) {
        super(message);
    }

    protected SprayException(String message, Throwable cause) {
        super(message, cause);
    }

    protected SprayException(Throwable cause) {
        super(cause);
    }

    protected SprayException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getBundleName() {
        return PREFIX + getBundleNameSuffix();
    }
    protected abstract String getBundleNameSuffix();

}
