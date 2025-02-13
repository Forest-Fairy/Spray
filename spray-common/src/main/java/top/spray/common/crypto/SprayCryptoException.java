package top.spray.common.crypto;

public class SprayCryptoException extends RuntimeException {
    public SprayCryptoException(String message) {
        super(message);
    }
    public SprayCryptoException(String message, Throwable cause) {
        super(message, cause);
    }
}
