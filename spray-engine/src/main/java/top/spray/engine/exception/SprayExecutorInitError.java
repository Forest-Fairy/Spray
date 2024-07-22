
package top.spray.engine.exception;

import cn.hutool.core.util.StrUtil;
import top.spray.core.i18n.Spray_i18n;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.i18n.SprayExecutorMessages_i18n;
import top.spray.engine.step.meta.SprayProcessStepMeta;

public class SprayExecutorInitError extends RuntimeException {
    private static final String message_key = "init.error";
    public SprayExecutorInitError(SprayProcessCoordinator coordinator, SprayProcessStepMeta stepMeta, Throwable cause) {
        super(
                StrUtil.format(
                        Spray_i18n.get(SprayExecutorMessages_i18n.class, message_key),
                        String.format(
                                "%s[%s](%s)", coordinator.getMeta().getName(), coordinator.getMeta().getId(), coordinator.getMeta().transactionId()
                        ),
                        String.format(
                                "%s[%s]", stepMeta.getName(), stepMeta.getId()),
                        cause.getMessage()),
                cause);
    }
}
