package top.spray.core.i18n;

import top.spray.core.util.SprayClassLoader;
import top.spray.core.util.SprayServiceUtil;
import top.spray.core.util.SprayUtf8s;

import java.util.*;

/**
 * implements it and declare in spi
 */
public interface Spray_i18n {
    /**
     * define the prefix of key to remove
     * @return prefix
     */
    String keyPrefix();

    /**
     * the source bundle name
     * @return source bundle name
     */
    String getBundleName();

    String BUNDLE_PREFIX = "spray.i18n.";
    static String get(Class<? extends Spray_i18n> clazz, String key) {
        String value = null;
        Map<String, Spray_i18n> I18N = SprayServiceUtil.loadServiceClassNameMapCache(Spray_i18n.class);
        Spray_i18n i18n = I18N.get(clazz.getName());
        if (i18n == null) {
            try {
                Class<?> aClass = SprayClassLoader.getDefault().loadClass(clazz.getName());
                i18n = (Spray_i18n) aClass.getDeclaredConstructor().newInstance();
            } catch (Exception ignored) {}
        }
        if (i18n != null) {
            String bundleName = i18n.getBundleName();
            if (! bundleName.startsWith(BUNDLE_PREFIX)) {
                bundleName = BUNDLE_PREFIX + bundleName;
            }
            ResourceBundle resourceBundle = ResourceBundle.getBundle(
                    bundleName, SprayUtf8s.ResourceBundleControl);
            if (resourceBundle != null) {
                if (i18n.keyPrefix() != null &&
                        !i18n.keyPrefix().isBlank() &&
                        key.startsWith(i18n.keyPrefix())) {
                    // remove key prefix
                    key = key.substring(i18n.keyPrefix().length());
                }
                value = resourceBundle.getString(key);
            } else {
                throw new RuntimeException("can not find resource bundle for " + bundleName);
            }
        }
        return value == null ? key : value;
    }

    static void setLocale(Locale locale) {
        if (locale != null) {
            Locale.setDefault(locale);
        }
    }

}
