package top.spray.processor.i18n;

/**
 * spray.i18n.engine.coordinator.suffix
 */
public class SprayCoordinator_i18n extends SprayEngine_i18n {
    @Override
    protected final String getBundleNameSuffix() {
        String suffix = getSuffix();
        return suffix == null || suffix.isEmpty() ?  "coordinator" : "coordinator." + suffix;
    }

    protected String getSuffix() {
        return "";
    }
}
