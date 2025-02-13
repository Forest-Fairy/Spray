package top.spray.core.global.config.i18n;

import top.spray.core.i18n.Spray_i18nBundleDef;

public class SpraySystemConfigurationName_i18n implements Spray_i18nBundleDef {
    private static final String BUNDLE_NAME = BUNDLE_PREFIX + "system.configuration.name";
    @Override
    public String getBundleName() {
        return BUNDLE_NAME;
    }
}
