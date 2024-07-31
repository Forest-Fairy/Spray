package top.spray.engine.design.event.annotation;

import top.spray.engine.design.event.constant.SprayEventPassByStrategy;

import java.lang.annotation.*;

/**
 * declare the data pass by strategy of the executor with yes or no
 * if it is configurable, do not use this annotation
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SprayEventPassBy {
    SprayEventPassByStrategy value();
}
