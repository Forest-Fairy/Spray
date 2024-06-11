package top.spray.core.engine.exception;

import top.spray.core.engine.meta.SprayBaseMeta;

public class SprayMetaError extends RuntimeException {
    public final SprayBaseMeta<?> meta;
    public SprayMetaError(SprayBaseMeta<?> meta, String message, Throwable cause) {
        super(message, cause);
        this.meta = meta;
    }

    public String getStepName() {
        return meta.getName();
    }
    public String getStepId() {
        return meta.getId();
    }
}
