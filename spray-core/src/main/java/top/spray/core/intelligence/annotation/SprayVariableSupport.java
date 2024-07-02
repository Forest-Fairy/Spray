package top.spray.core.intelligence.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Target(ElementType.METHOD)
public @interface SprayVariableSupport {
}
