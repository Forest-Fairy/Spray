package top.spray.engine.process.processor.data.exception;


public class SprayDataReadingError extends SprayDataException {
    public SprayDataReadingError(Throwable cause) {
        super(cause, "reading.error", cause.getMessage());
    }

}
