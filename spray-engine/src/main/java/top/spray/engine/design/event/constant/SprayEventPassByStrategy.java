package top.spray.engine.design.event.constant;

import java.lang.annotation.Annotation;

public enum SprayEventPassByStrategy {
    YES,
    NO,
    CONFIGURABLE,
    ;

    public static SprayEventPassByStrategy StrategyOf(Class<?> cz) {
        try {
            for (Annotation annotation : cz.getAnnotations()) {
                if (annotation.getClass().getName().endsWith("SprayEventPassBy")) {
                    return SprayEventPassByStrategy.valueOf(String.valueOf(
                            annotation.getClass().getMethod("value")
                                    .invoke(annotation)));
                }
            }
        } catch (Exception ignored) {}
        // else is configurable
        return CONFIGURABLE;
    }
}
