package top.spray.engine.process.infrastructure.meta;


import top.spray.engine.exception.SprayEngineException;

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
