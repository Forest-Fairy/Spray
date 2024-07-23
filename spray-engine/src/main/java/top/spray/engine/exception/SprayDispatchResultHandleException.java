package top.spray.engine.exception;

import cn.hutool.core.util.StrUtil;
import top.spray.core.i18n.Spray_i18n;
import top.spray.engine.coordinate.handler.result.SprayDataDispatchResultHandler;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.i18n.SprayCoordinatorMessages_i18n;

public class SprayDispatchResultHandleException extends RuntimeException {
    private static final String message_key = "coordinate.dispatch.result.handle.error";
    public SprayDispatchResultHandleException(SprayDataDispatchResultHandler handler,
                                              SprayProcessCoordinatorMeta coordinatorMeta,
                                              String dataKey, Throwable cause) {
        super(
                StrUtil.format(
                        Spray_i18n.get(SprayCoordinatorMessages_i18n.class, message_key),
                        handler.getClass().getName(),
                        String.format("%s[%s]",
                                coordinatorMeta.getName(), coordinatorMeta.transactionId()),
                        dataKey, cause.getMessage()),
                cause);
    }
}
