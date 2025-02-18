package top.spray.core.i18n;

import top.spray.common.bean.SprayServiceUtil;
import top.spray.common.data.SprayStringUtils;
import top.spray.common.tools.Sprays;
import top.spray.core.dynamic.SprayClassLoaderUtil;

import java.lang.reflect.Constructor;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * implements it and declare in spi
 * <h4>spray.i18n.</h4>
 */
@FunctionalInterface
public interface SprayResourceBundleDef {
    /**
     * the source bundle name
     * @return source bundle name
     */
    String getBundleName();

    String BUNDLE_PREFIX = "spray.i18n.";
    static String get(String type, Class<?> clazz, String key, Object... args) {
        Objects.requireNonNull(key, "key should not be null");
        if (key.contains(" ")) {
            return key;
        }
        String value = null;
        Map<String, SprayResourceBundleDef> I18N = SprayServiceUtil.loadServiceClassNameMapCache(SprayResourceBundleDef.class);
        SprayResourceBundleDef i18n = I18N.get(clazz.getName());
        if (i18n == null) {
            if (SprayResourceBundleDef.class.isAssignableFrom(clazz)) {
                try {
                    Class<?> aClass = SprayClassLoaderUtil.DEFAULT.loadClass(clazz.getName());
                    Constructor<?> constructor = aClass.getConstructor();
                    constructor.setAccessible(true);
                    i18n = (SprayResourceBundleDef) constructor.newInstance();
                } catch (Exception ignored) {}
            } else {
                try {
                    SprayResourceBundle annotation = clazz.getAnnotation(SprayResourceBundle.class);
                    if (!annotation.value().isBlank()) {
                        i18n = annotation::value;
                    }
                } catch (Exception ignored) {}
            }
        }
        if (i18n != null) {
            String bundleName = i18n.getBundleName();
            if (! bundleName.startsWith(BUNDLE_PREFIX)) {
                bundleName = BUNDLE_PREFIX + bundleName;
            }
            if (type != null && !type.isBlank()) {
                bundleName = bundleName + "." + type;
            }
            ResourceBundle resourceBundle = ResourceBundle.getBundle(
                    bundleName, Sprays.ResourceBundleControl);
            if (resourceBundle != null) {
//                if (i18n.keyPrefix() != null &&
//                        !i18n.keyPrefix().isBlank() &&
//                        key.startsWith(i18n.keyPrefix())) {
//                    // remove key prefix
//                    key = key.substring(i18n.keyPrefix().length());
//                }
                value = resourceBundle.getString(key);
            } else {
                throw new RuntimeException("can not find resource bundle for " + bundleName);
            }
        }
        return value == null ? key : SprayStringUtils.format(value, args);
    }

    static void setLocale(Locale locale) {
        if (locale != null) {
            Locale.setDefault(locale);
        }
    }

}
