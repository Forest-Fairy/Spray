package top.spray.demo.core.spring;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Modifier;

public class SpringClassUtil {
    private SpringClassUtil() {}

    public static boolean isSpringBeanClass(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        //是否是接口
        if (clazz.isInterface()) {
            return false;
        }
        //是否是抽象类
        if (Modifier.isAbstract(clazz.getModifiers())) {
            return false;
        }
        if (clazz.getAnnotation(Component.class) != null) {
            return true;
        }
        if (clazz.getAnnotation(Repository.class) != null) {
            return true;
        }
        if (clazz.getAnnotation(Service.class) != null) {
            return true;
        }
        return false;
    }
    /**
     * 类名首字母小写 作为spring容器beanMap的key
     */
    public static String transformName(String className) {
        String tmpStr = className.substring(className.lastIndexOf(".") + 1);
        return tmpStr.substring(0, 1).toLowerCase() + tmpStr.substring(1);
    }
}
