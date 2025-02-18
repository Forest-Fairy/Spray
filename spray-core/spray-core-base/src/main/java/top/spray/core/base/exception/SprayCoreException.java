package top.spray.core.base.exception;

public class SprayCoreException extends SprayException {
    public SprayCoreException() {
    }

    public SprayCoreException(String message, Object... params) {
        super(message, params);
    }

    public SprayCoreException(Throwable cause, String message, Object... params) {
        super(cause, message, params);
    }

    public SprayCoreException(Throwable cause) {
        super(cause);
    }

    public SprayCoreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... params) {
        super(message, cause, enableSuppression, writableStackTrace, params);
    }

    @Override
    protected String getBundleNameSuffix() {
        return "core";
    }
}
