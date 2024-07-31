package top.spray.engine.event.util;

import org.apache.commons.lang3.StringUtils;
import top.spray.engine.event.model.SprayEvent;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class SpraySubscribes {
    private SpraySubscribes() {}

    public static void readSubscribeMethodsOnClass(Map<String, List<Method>> subscribes, Class<?> cz) {
        for (Method method : cz.getDeclaredMethods()) {
            String subscribeEvents = null;
            try {
                for (Annotation annotation : method.getAnnotations()) {
                    if (annotation.getClass().getName().endsWith("SpraySubscribe")) {
                        subscribeEvents = annotation.getClass().getMethod("value").invoke(annotation).toString();
                    }
                }
            } catch (Exception ignored) {}
            if (StringUtils.isNotBlank(subscribeEvents)) {
                // has annotation
                // TODO handle ex
                if (Modifier.isStatic(method.getModifiers())) {
                    throw new IllegalArgumentException("subscribe method should not be static");
                }
                if (method.getParameterTypes().length > 1) {
                    throw new IllegalArgumentException("subscribe method should have only one parameter");
                }
                if (method.getParameterTypes().length > 0 && ! SprayEvent.class.isAssignableFrom(method.getParameterTypes()[0])) {
                    throw new IllegalArgumentException("subscribe method should have only one parameter whose type is spray event");
                }
                for (String eventName : subscribeEvents.split(",")) {
                    subscribes
                            .computeIfAbsent(eventName, k -> new ArrayList<>())
                            .add(method);
                }
            }
        }
    }
}
