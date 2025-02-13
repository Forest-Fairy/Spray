package top.spray.processor.infrustructure.analyse;

import top.spray.processor.exception.base.SprayEngineException;

/**
 * spray.i18n.exception.engine.analyse
 */
public class SprayAnalyseException extends SprayEngineException {
    public SprayAnalyseException(String key, Object... params) {
        super(key, params);
    }
    public SprayAnalyseException(Throwable cause, String key, Object... params) {
        super(cause, key, params);
    }

    @Override
    protected String getTypeBundleNameSuffix() {
        return "analyse";
    }
}
