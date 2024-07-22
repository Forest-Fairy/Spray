package top.spray.engine.exception;

import cn.hutool.core.util.StrUtil;
import top.spray.core.i18n.Spray_i18n;
import top.spray.engine.i18n.SprayExecutorMessages_i18n;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

public class SprayExecuteError extends RuntimeException {
    private static final String message_key = "execute.error";
    public SprayExecuteError(SprayProcessStepExecutor executor, Throwable cause) {
        super(
                StrUtil.format(
                        Spray_i18n.get(SprayExecutorMessages_i18n.class, message_key),
                        String.format("%s[%s]",
                                executor.getMeta().getName(),
                                executor.getMeta().getId()),
                        cause.getMessage()),
                cause);
    }
    public SprayExecuteError(SprayProcessStepExecutor executor, Throwable cause, String msg) {
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
