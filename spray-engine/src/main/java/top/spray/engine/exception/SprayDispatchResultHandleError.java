package top.spray.engine.exception;

import cn.hutool.core.util.StrUtil;
import top.spray.core.i18n.Spray_i18n;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.coordinate.handler.result.SprayDataDispatchResultHandler;
import top.spray.engine.i18n.SprayCoordinatorMessages_i18n;
import top.spray.engine.i18n.SprayExecutorMessages_i18n;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

public class SprayDispatchResultHandleError extends RuntimeException {
    private static final String message_key = "coordinate.dispatch.result.handle.error";
    public SprayDispatchResultHandleError(SprayDataDispatchResultHandler handler,
                                          SprayProcessCoordinator coordinator,
                                          String dataKey, Throwable cause) {
        super(
                StrUtil.format(
                        Spray_i18n.get(SprayCoordinatorMessages_i18n.class, message_key),
                        String.format("%s[%s]",
                                executor.getMeta().getName(),
                                executor.getMeta().getId()),
                        cause.getMessage()),
                cause);
    }
}
