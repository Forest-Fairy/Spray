package top.spray.engine.i18n;

import top.spray.core.i18n.SprayResourceBundleDef;

/**
 * define the base i18n bundle path with
 * <h4>spray.i18n.engine.suffix</h4>
 */
public abstract class SprayEngineBundleDef implements SprayResourceBundleDef {
    public static final String BUNDLE_NAME_PREFIX = "engine.";

    @Override
    public final String getBundleName() {
        return BUNDLE_NAME_PREFIX + getBundleNameSuffix();
    }

    protected abstract String getBundleNameSuffix();

}
