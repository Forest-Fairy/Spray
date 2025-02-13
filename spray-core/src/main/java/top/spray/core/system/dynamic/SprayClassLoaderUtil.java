package top.spray.core.system.dynamic;

import java.io.File;
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
        SprayClassLoader sprayClassLoader = new SprayClassLoader(parent);
        sprayClassLoader.addClasses(new File(url));
        return sprayClassLoader;
    }

    public static final SprayClassLoader DEFAULT = new SprayClassLoader(Thread.currentThread().getContextClassLoader());
    public static ClassLoader getCurrentClassloader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
