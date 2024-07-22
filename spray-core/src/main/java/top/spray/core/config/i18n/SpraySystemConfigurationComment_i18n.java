package top.spray.core.config.i18n;

import top.spray.core.config.model.SpraySystemConfiguration;
import top.spray.core.i18n.Spray_i18n;

public class SpraySystemConfigurationComment_i18n implements Spray_i18n {
    private static final String BUNDLE_NAME = BUNDLE_PREFIX + "system.configuration.comment";
    @Override
    public String keyPrefix() {
        return null;
    }
    @Override
    public String getBundleName() {
        return BUNDLE_NAME;
    }
}
