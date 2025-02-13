package top.spray.processor.exception.data;


import top.spray.processor.exception.base.SprayDataException;

public class SprayDataReadingError extends SprayDataException {
    public SprayDataReadingError(Throwable cause) {
        super(cause, "reading.error", cause.getMessage());
    }

}
