package top.spray.engine.step.design.constant;

import java.lang.annotation.Annotation;

public enum SprayDataPassByStrategy {
    YES,
    NO,
    CONFIGURABLE,
    ;

    public static SprayDataPassByStrategy CanDataPassBy(Class<?> cz) {
        try {
            for (Annotation annotation : cz.getAnnotations()) {
                if (annotation.getClass().getName().endsWith("SprayDataPassByStrategy")) {
                    return Boolean.parseBoolean(String.valueOf(annotation.getClass()
                            .getMethod("value").invoke(annotation))) ?
                            SprayDataPassByStrategy.YES : SprayDataPassByStrategy.NO;
                }
            }
        } catch (Exception ignored) {}
        // else is configurable
        return CONFIGURABLE;
    }
}
