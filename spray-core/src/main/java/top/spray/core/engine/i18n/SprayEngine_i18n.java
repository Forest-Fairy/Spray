package top.spray.core.engine.i18n;

import top.spray.core.i18n.Spray_i18n;
import top.spray.core.i18n.message.SprayMessageObject;

/**
 * define the base i18n bundle path with
 * <h4>spray.i18n.engine.</h4>
 */
public abstract class SprayEngine_i18n implements SprayMessageObject {
    private static final String BUNDLE_NAME_PREFIX = BUNDLE_PREFIX + "engine.";

    @Override
    public String keyPrefix() {
        return null;
    }
    @Override
    public String getBundleName() {
        return BUNDLE_NAME_PREFIX + getBundleNameSuffix();
    }

    protected abstract String getBundleNameSuffix();
}
