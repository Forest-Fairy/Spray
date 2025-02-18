package top.spray.core.type;

import top.spray.core.i18n.SprayResourceBundleDef;

/**
 * define the base i18n bundle path with
 * <h4>spray.i18n.type.suffix</h4>
 */
public abstract class SprayType_i18nBundleDef implements SprayResourceBundleDef {
    private static final String BUNDLE_NAME_PREFIX = BUNDLE_PREFIX + "type.";

    @Override
    public final String getBundleName() {
        return BUNDLE_NAME_PREFIX + getTypeNameSuffix();
    }

    protected abstract String getTypeNameSuffix();
}
