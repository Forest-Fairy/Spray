package top.spray.core.i18n.message;

import cn.hutool.core.util.StrUtil;
import top.spray.core.i18n.Spray_i18n;

public interface SprayMessageObject extends Spray_i18n {
    default String keyPrefix() {
        return null;
    }

    default String format(String key, Object... args) {
        return Spray_i18n.get(this.getClass(), StrUtil.format(key, args));
    }

}
