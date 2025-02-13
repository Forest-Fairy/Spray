package top.spray.common.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Sprays {
    public static final Sprays INSTANCE = new Sprays();
    private Sprays() {}
    public static final Charset UTF_8 = StandardCharsets.UTF_8;
    public static final ResourceBundle.Control ResourceBundleControl =
            new ResourceBundle.Control() {
                @Override
                public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IOException {
                    String bundleName = toBundleName(baseName, locale);
                    String resourceName = toResourceName(bundleName, "properties");
                    try (InputStream stream = Objects.requireNonNull(loader.getResourceAsStream(resourceName))) {
                        return new PropertyResourceBundle(new InputStreamReader(stream, Sprays.UTF_8));
                    }
                }
            };
    public static byte[] getBytes(String str) {
        return str == null ? new byte[0] : str.getBytes(UTF_8);
    }
    public static String toString(byte[] content) {
        return content == null ? null : new String(content, UTF_8);
    }

    public static final String FILE_SEPARATOR = File.separator;
    public static final String PATH_SEPARATOR = File.pathSeparator;

    public static final String SystemPropertyKey_LineSeparator = "line.separator";
    public static final String SystemPropertyKey_ClassPath = "java.class.path";


    public static String WebLineSeparator() {
        return "\r\n";
    }
    public static String TextLineSeparator() {
        return System.getProperty(SystemPropertyKey_LineSeparator);
    }
    public static String ClassPath() {
        return System.getProperty(SystemPropertyKey_ClassPath);
    }

    public static String UUID() {
        return UUID.randomUUID().toString();
    }

    @SafeVarargs
    public static <T> T[] asArray(T... t) {
        return t;
    }

}
