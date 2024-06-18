package top.spray.engine.step.handler.factory;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
@Documented
public @interface SprayBeforeExecutorCreate {
    Class<? extends SprayExecutorBeforeCreateHandler> handlerBeforeCreate();
}
