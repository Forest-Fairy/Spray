package top.spray.engine.exception;

import cn.hutool.core.util.StrUtil;
import top.spray.core.i18n.Spray_i18n;
import top.spray.engine.remoting.SprayProcessCoordinatorAdapter;
import top.spray.engine.i18n.SprayCoordinatorMessages_i18n;

public class SprayCoordinatorAdapterUnsupportedException extends RuntimeException {
    private static final String message_key = "coordinate.adapter.call.unsupported";
    public SprayCoordinatorAdapterUnsupportedException(SprayProcessCoordinatorAdapter adapter) {
        super(
                StrUtil.format(Spray_i18n.get(SprayCoordinatorMessages_i18n.class, message_key),
                        adapter.getClass().getName()));
    }
}
