
package top.spray.engine.process.exception.coordinate;

import cn.hutool.core.util.StrUtil;
import top.spray.core.i18n.SprayResourceBundleDef;
import top.spray.processor.exception.base.SprayEngineException;
import top.spray.engine.process.processor.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.process.processor.execute.step.meta.SprayProcessExecuteStepMeta;

public class SprayFailedInitExecutorException extends SprayEngineException {
    private static final String message_key = "executor.init.failed";
    public SprayFailedInitExecutorException(SprayProcessCoordinator coordinator, SprayProcessExecuteStepMeta stepMeta, Throwable cause) {
        super(
                StrUtil.format(
                        SprayResourceBundleDef.get(SprayFailedInitExecutorException.class, message_key),
                        String.format(
                                "%s[%s](%s)", coordinator.getMeta().getName(), coordinator.getMeta().getId(), coordinator.transactionId()),
                        String.format(
                                "%s[%s]", stepMeta.getName(), stepMeta.getId()),
                        cause.getMessage()),
                cause);
    }

    @Override
    protected String getTypeBundleNameSuffix() {
        return "execute";
    }

}
