package top.spray.core.system.intelligence.annotation;

import top.spray.core.global.config.util.SpraySystemConfigurations;

import java.lang.annotation.*;

/**
 * an annotation for marking a class which needs to be analyzed to generate a xml file that contains its information
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Target(ElementType.TYPE)
public @interface SprayClassInfoAutoAnalyse {
    /** empty means {@link SpraySystemConfigurations#sprayVersion()} */
    String version() default "";
    /** Priority is annotation > doc comment > className  */
    String description() default "";
}
