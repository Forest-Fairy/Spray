package top.spray.engine.process.exception.data;


import top.spray.engine.process.exception.base.SprayDataException;

public class SprayDataReadingError extends SprayDataException {
    public SprayDataReadingError(Throwable cause) {
        super(cause, "reading.error", cause.getMessage());
    }

}
