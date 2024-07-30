package top.spray.engine.step.design.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SpraySubscribe {
    String[] value();
}
