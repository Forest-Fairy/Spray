package top.spray.core.exception;

import top.spray.common.data.SprayStringUtils;
import top.spray.core.i18n.SprayResourceBundleDef;

/**
 * define resource bundle with
 * <h4>spray.i18n.exception.suffix</h4>
 * default is
 * <h4>spray.i18n.exception.messages</h4>
 */
public abstract class SprayException extends RuntimeException implements SprayResourceBundleDef {
    private static final String PREFIX = BUNDLE_PREFIX + "exception.";
    private static final String DEFAULT = PREFIX + "messages";

    protected final String message;

    protected SprayException() {
        this.message = null;
    }

    protected SprayException(String message, Object... params) {
        super(message);
        this.message = toI18n(message, params);
    }

    protected SprayException(Throwable cause, String message, Object... params) {
        super(message, cause);
        this.message = toI18n(message, params);
    }

    protected SprayException(Throwable cause) {
        super(cause);
        this.message = (cause==null ? null : cause.toString());
    }

    protected SprayException(String message, Throwable cause,
                             boolean enableSuppression, boolean writableStackTrace,
                             Object... params) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.message = toI18n(message, params);
    }
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getLocalizedMessage() {
        return this.getMessage();
    }

    @Override
    public final String getBundleName() {
        // return spray.i18n.exception.messages otherwise spray.i18n.exception.suffix
        return getBundleNameSuffix() == null ? DEFAULT :
                PREFIX + getBundleNameSuffix();
    }

    protected abstract String getBundleNameSuffix();

    protected String toI18n(String message, Object... params) {
        String i18n = message == null ? null :
                SprayResourceBundleDef.get(this.getClass(), message);
        return SprayStringUtils.format(i18n, params);
    }

}
