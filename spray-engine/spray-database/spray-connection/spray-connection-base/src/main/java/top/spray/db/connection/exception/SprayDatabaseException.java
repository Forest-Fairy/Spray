package top.spray.db.connection.exception;

import top.spray.core.exception.SprayException;

/**
 * define resource bundle with
 * <h4>spray.i18n.exception.database</h4>
 */
public class SprayDatabaseException extends SprayException {
    public SprayDatabaseException() {
    }

    public SprayDatabaseException(String message, Object... params) {
        super(message, params);
    }

    public SprayDatabaseException(Throwable cause, String message, Object... params) {
        super(cause, message, params);
    }

    public SprayDatabaseException(Throwable cause) {
        super(cause);
    }

    @Override
    protected final String getBundleNameSuffix() {
        String suffix = getSecSuffix();
        return suffix == null || suffix.isEmpty() ?  "database.messages" : "database"+"."+suffix;
    }
    protected String getSecSuffix() {
        return "";
    }
}
