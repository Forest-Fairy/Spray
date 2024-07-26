package top.spray.core.dynamic.util;

import top.spray.core.dynamic.loader.SprayClassLoader;

import java.net.URL;
import java.util.regex.Pattern;

public class SprayClassLoaderUtil {
    private static final Pattern Link_Pattern = Pattern.compile("^[A-Za-z]+://");

    public static SprayClassLoader loadWithJarFile(
            String url, ClassLoader parent) throws Exception {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("url can not be null or blank");
        }
        if (!Link_Pattern.matcher(url).find()) {
            url = "file://" + url;
        }
        return new SprayClassLoader(new URL[]{new URL(url)}, parent);
    }
}
