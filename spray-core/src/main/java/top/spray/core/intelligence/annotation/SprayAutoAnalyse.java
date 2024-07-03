package top.spray.core.intelligence.annotation;

import java.lang.annotation.*;

/**
 * an annotation for marking a class which needs to be analyzed to generate a xml file that contains its information
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Target(ElementType.TYPE)
public @interface SprayAutoAnalyse {
    /** empty means {@link top.spray.core.util.SpraySystemUtil#getSystemVersion()} */
    String version() default "";
    /** Priority is annotation > doc comment > className  */
    String description() default "";
}
