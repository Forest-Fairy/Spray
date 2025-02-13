package top.spray.common.data;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;
import top.spray.common.tools.Sprays;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class SprayStringUtils extends StringUtils {
    public static final SprayStringUtils INSTANCE = new SprayStringUtils();
    private SprayStringUtils() {}

    public static String format(String template, Object... params) {
        if (template == null) {
            return null;
        }
        if (template.contains("{0}")) {
            return StrUtil.indexedFormat(template, params);
        }
        return StrUtil.format(template, params);
    }



    public static byte[] bytes(CharSequence str) {
        return bytes(str, Sprays.UTF_8);
    }
    public static byte[] bytes(CharSequence str, Charset charset) {
        if (str == null) {
            return null;
        } else {
            return null == charset ? str.toString().getBytes() : str.toString().getBytes(charset);
        }
    }

    public static ByteBuffer byteBuffer(CharSequence str) {
        return byteBuffer(str, Sprays.UTF_8);
    }
    public static ByteBuffer byteBuffer(CharSequence str, Charset charset) {
        return ByteBuffer.wrap(bytes(str, charset));
    }
}
