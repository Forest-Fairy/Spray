package top.spray.processor.i18n;

import top.spray.core.i18n.SprayResourceBundleDef;

/**
 * define the base i18n bundle path with
 * <h4>spray.i18n.engine.suffix</h4>
 */
public abstract class SprayEngine_i18n implements SprayResourceBundleDef {
    private static final String BUNDLE_NAME_PREFIX = BUNDLE_PREFIX + "engine.";

    @Override
    public final String getBundleName() {
        return BUNDLE_NAME_PREFIX + getBundleNameSuffix();
    }

    protected abstract String getBundleNameSuffix();

}
