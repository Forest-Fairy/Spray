package top.spray.engine.exception;

import cn.hutool.core.util.StrUtil;
import top.spray.core.i18n.Spray_i18n;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.i18n.SprayExecutorMessages_i18n;
import top.spray.engine.step.meta.SprayProcessStepMeta;

public class SprayExecutorGenerateError extends RuntimeException {
    private static final String message_key = "executor.generate.error";
    public SprayExecutorGenerateError(SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessStepMeta stepMeta, Throwable cause) {
        super(
                StrUtil.format(
                        Spray_i18n.get(SprayExecutorMessages_i18n.class, message_key),
                        String.format("%s[%s]", coordinatorMeta.getName(), coordinatorMeta.transactionId()),
                        String.format("%s[%s]", stepMeta.getName(), stepMeta.getId()),
                        cause.getMessage()
                ),
                cause);
    }
}
