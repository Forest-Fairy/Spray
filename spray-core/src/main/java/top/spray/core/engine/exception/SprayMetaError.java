package top.spray.core.engine.exception;

import top.spray.core.engine.meta.SprayBaseMeta;
import top.spray.core.exception.SprayException;

public class SprayMetaError extends SprayException {
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

    @Override
    protected String getBundleNameSuffix() {
        return null;
    }
}
