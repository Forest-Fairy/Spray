package top.spray.demo.core.spring;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import top.spray.core.dynamic.listener.SprayClassLoaderListener;
import top.spray.core.dynamic.loader.SprayClassLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SprayClassPostProcessWithSpringBean implements SprayClassLoaderListener {
    private static final BeanDefinitionRegistry BEAN_DEFINITION_REGISTRY;
    static {
        BEAN_DEFINITION_REGISTRY = SpringUtil.getBean(BeanDefinitionRegistry.class);
    }

    private static final Map<SprayClassLoader, List<String>> loaded_beans = new ConcurrentHashMap<>();

    @Override
    public void onClassDefined(SprayClassLoader classLoader, Class<?> clazz) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        String beanName = SpringClassUtil.transformName(clazz.getName());
        loaded_beans.computeIfAbsent(classLoader, (key) -> new ArrayList<>()).add(beanName);
        BEAN_DEFINITION_REGISTRY.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
    }

    @Override
    public void onClassLoaderClose(SprayClassLoader classLoader) {
        List<String> beans = loaded_beans.get(classLoader);
        if (beans != null) {
            beans.forEach(BEAN_DEFINITION_REGISTRY::removeBeanDefinition);
            loaded_beans.remove(classLoader);
        }
    }
}
