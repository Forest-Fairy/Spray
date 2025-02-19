package top.spray.core.i18n;

import java.lang.annotation.*;

@Documented
@Inherited // can be transferred by extends
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SprayResourceBundle {
    String value();

    String BUNDLE_NAME_PREFIX = "spray.i18n.";
    String CONFIGURATION = BUNDLE_NAME_PREFIX + "configuration";
    String TYPE = BUNDLE_NAME_PREFIX + "type";
    String ENGINE = BUNDLE_NAME_PREFIX + "engine";
}
