package top.spray.core.system.intelligence.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Target(ElementType.FIELD)
public @interface SprayComplicateProp {
    /**
     * giving a json to describe the prop
     */
    String value();
}
