package top.spray.db.connection.exception;

/**
 * define resource bundle with
 * <h4>spray.i18n.exception.database.sql</h4>
 */
public class SpraySqlException extends SprayDatabaseException {
    public SpraySqlException() {
    }

    public SpraySqlException(String message, Object... params) {
        super(message, params);
    }

    public SpraySqlException(Throwable cause, String message, Object... params) {
        super(cause, message, params);
    }

    public SpraySqlException(Throwable cause) {
        super(cause);
    }

}
