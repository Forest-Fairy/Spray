package top.spray.engine.exception;

import cn.hutool.core.util.StrUtil;
import top.spray.core.i18n.Spray_i18n;
import top.spray.engine.i18n.SprayExecutorMessages_i18n;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.meta.SprayProcessStepMeta;

public class SprayExecuteException extends RuntimeException {
    private static final String message_key = "execute.error";
    public SprayExecuteException(SprayProcessStepMeta stepMeta, Throwable cause) {
        super(
                StrUtil.format(
                        Spray_i18n.get(SprayExecutorMessages_i18n.class, message_key),
                        String.format("%s[%s]",
                                stepMeta.getName(),
                                stepMeta.getId()),
                        cause.getMessage()),
                cause);
    }
    public SprayExecuteException(SprayProcessStepExecutor executor, Throwable cause, String msg) {
        super(
                StrUtil.format(
                        Spray_i18n.get(SprayExecutorMessages_i18n.class, message_key),
                        String.format("%s[%s]",
                                executor.getMeta().getName(),
                                executor.getMeta().getId()),
                        (msg == null || msg.isBlank()) ? cause.getMessage() : msg),
                cause);
    }
}
