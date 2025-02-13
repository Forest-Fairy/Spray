package top.spray.processor.exception;


import top.spray.processor.exception.base.SprayEngineException;
import top.spray.processor.infrustructure.meta.SprayBaseMeta;

public class SprayMetaError extends SprayEngineException {
    public SprayMetaError(String message, Object... params) {
        super(message, params);
    }
    public SprayMetaError(SprayBaseMeta meta, String message, Throwable cause) {
        super(cause, message, meta.getId(), meta.getName());
    }

    @Override
    protected String getTypeBundleNameSuffix() {
        return "meta";
    }

}
