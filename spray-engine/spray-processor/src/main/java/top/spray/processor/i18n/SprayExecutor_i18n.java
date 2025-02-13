package top.spray.processor.i18n;

/**
 * spray.i18n.engine.executor.suffix
 */
public class SprayExecutor_i18n extends SprayEngine_i18n {
    @Override
    protected String getBundleNameSuffix() {
        String suffix = getSuffix();
        return suffix == null || suffix.isEmpty() ?  "executor" : "executor." + suffix;
    }
    protected String getSuffix() {
        return "";
    }
}
