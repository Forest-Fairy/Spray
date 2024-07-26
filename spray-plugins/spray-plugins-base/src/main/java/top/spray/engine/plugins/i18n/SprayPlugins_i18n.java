package top.spray.engine.plugins.i18n;

import top.spray.core.engine.i18n.SprayEngine_i18n;

/**
 * define the base i18n bundle path with
 * <h4>spray.i18n.engine.plugins.</h4>
 */
public abstract class SprayPlugins_i18n extends SprayEngine_i18n {

    @Override
    protected String getBundleNameSuffix() {
        return "plugins." + getPluginsBundleNameSuffix();
    }

    protected abstract String getPluginsBundleNameSuffix();
}
