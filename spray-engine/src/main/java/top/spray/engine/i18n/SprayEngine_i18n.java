package top.spray.engine.i18n;

import top.spray.core.i18n.Spray_i18n;

public abstract class SprayEngine_i18n implements Spray_i18n {
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
