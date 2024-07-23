package top.spray.engine.exception;

import cn.hutool.core.util.StrUtil;
import top.spray.core.i18n.Spray_i18n;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.i18n.SprayCoordinatorMessages_i18n;

public class SprayCoordinatorGenerateError extends RuntimeException {
    private static final String message_key = "coordinator.generate.error";
    public SprayCoordinatorGenerateError(SprayProcessCoordinatorMeta coordinatorMeta, Throwable cause) {
        super(
                StrUtil.format(
                        Spray_i18n.get(SprayCoordinatorMessages_i18n.class, message_key),
                        coordinatorMeta.getName(),
                        String.format("%s[%s]", coordinatorMeta.getId(), coordinatorMeta.transactionId()),
                        cause.getMessage()
                ),
                cause);
    }
}
