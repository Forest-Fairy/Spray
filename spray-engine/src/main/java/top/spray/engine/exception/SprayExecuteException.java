package top.spray.engine.exception;

import cn.hutool.core.util.StrUtil;
import top.spray.core.i18n.Spray_i18n;
import top.spray.engine.i18n.SprayExecutorMessages_i18n;
import top.spray.engine.step.executor.SprayExecutorDefinition;

public class SprayExecuteException extends RuntimeException {
    private static final String message_key = "execute.error";
    private final SprayExecutorDefinition executorDefinition;

    public SprayExecuteException(SprayExecutorDefinition executorDefinition, Throwable cause) {
        this.executorDefinition = executorDefinition;
        super(
                StrUtil.format(
                        Spray_i18n.get(SprayExecutorMessages_i18n.class, message_key),
                        String.format("%s[%s]",
                                executorDefinition.getName(),
                                executorDefinition.getId()),
                        cause.getMessage()),
                cause);
    }
    public SprayExecuteException(SprayExecutorDefinition executorDefinition, Throwable cause, String msg) {
        this.executorDefinition = executorDefinition;
        super(
                StrUtil.format(
                        Spray_i18n.get(SprayExecutorMessages_i18n.class, message_key),
                        String.format("%s[%s]",
                                executor.getMeta().getName(),
                                executor.getMeta().getId()),
                        (msg == null || msg.isBlank()) ? cause.getMessage() : msg),
                cause);
    }

    public SprayExecutorDefinition getExecutorDefinition() {
        return executorDefinition;
    }
}
