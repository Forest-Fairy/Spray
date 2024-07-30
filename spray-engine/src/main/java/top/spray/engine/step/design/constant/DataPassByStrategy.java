package top.spray.engine.step.design.constant;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public enum DataPassByStrategy {
    YES,
    NO,
    CONFIGURABLE,
    ;

    public static DataPassByStrategy CanDataPassBy(Class<?> cz, Object bean) {
        DataPassByStrategy strategy = CONFIGURABLE;
        try {
            Method canDataPassByMethod = null;
            for (Method method : cz.getDeclaredMethods()) {
                if (method.getReturnType().getName().equals(DataPassByStrategy.class.getName())) {
                    canDataPassByMethod = method;
                }
            }
            if (! Modifier.isStatic(canDataPassByMethod.getModifiers())) {
                if (bean == null) {
                    bean = cz.getDeclaredConstructor().newInstance();
                }
                strategy = DataPassByStrategy.valueOf(canDataPassByMethod.invoke(bean).toString());
            } else {
                strategy = DataPassByStrategy.valueOf(canDataPassByMethod.invoke(null).toString());
            }
        } catch (Exception ignored) {}
        return strategy;
    }
}
