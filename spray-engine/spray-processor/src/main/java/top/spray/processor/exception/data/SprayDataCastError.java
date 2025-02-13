package top.spray.processor.exception.data;

import top.spray.processor.exception.base.SprayDataException;

public class SprayDataCastError extends SprayDataException {
    public SprayDataCastError(Class<?> fromType, Class<?> toType, Throwable cause) {
        super(cause, "cast.other_error", fromType.getName(), toType.getName());
    }
    public SprayDataCastError(String colName, Class<?> fromType, Throwable cause) {
        super(cause, "cast.field_error", colName, fromType.getName());
    }

}
