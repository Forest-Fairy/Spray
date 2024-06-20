package top.spray.engine.step.handler.create;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
@Documented
public @interface SprayExecutorCreationFactory {
    Class<? extends SprayExecutorFactory> value();
}
