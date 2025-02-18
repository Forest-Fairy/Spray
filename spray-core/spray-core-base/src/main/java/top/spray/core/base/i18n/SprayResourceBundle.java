package top.spray.core.base.i18n;

import java.lang.annotation.*;

@Documented
// 子类自动继承本注解
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SprayResourceBundle {
    String value();
}
