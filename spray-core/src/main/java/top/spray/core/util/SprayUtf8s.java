package top.spray.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class SprayUtf8s {
    public static final Charset Charset = StandardCharsets.UTF_8;
    public static final ResourceBundle.Control ResourceBundleControl = new ResourceBundle.Control() {
        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IOException {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            try (InputStream stream = Objects.requireNonNull(loader.getResourceAsStream(resourceName))) {
                return new PropertyResourceBundle(new InputStreamReader(stream, SprayUtf8s.Charset));
            }
        }
    };
}
