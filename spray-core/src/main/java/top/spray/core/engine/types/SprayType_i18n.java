package top.spray.core.engine.types;

import top.spray.core.engine.i18n.SprayEngine_i18n;

/**
 * define the base i18n bundle path with
 * <h4>spray.i18n.engine.types.</h4>
 */
public abstract class SprayType_i18n extends SprayEngine_i18n {
    @Override
    protected String getBundleNameSuffix() {
        return "types." + getTypeName();
    }
    protected abstract String getTypeName();
}
