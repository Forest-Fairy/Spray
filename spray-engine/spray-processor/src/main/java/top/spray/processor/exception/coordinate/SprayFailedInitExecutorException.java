
package top.spray.processor.exception.coordinate;

import cn.hutool.core.util.StrUtil;
import top.spray.core.i18n.Spray_i18nBundleDef;
import top.spray.processor.exception.base.SprayEngineException;
import top.spray.processor.process.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.processor.process.execute.step.meta.SprayProcessExecuteStepMeta;

public class SprayFailedInitExecutorException extends SprayEngineException {
    private static final String message_key = "executor.init.failed";
    public SprayFailedInitExecutorException(SprayProcessCoordinator coordinator, SprayProcessExecuteStepMeta stepMeta, Throwable cause) {
        super(
                StrUtil.format(
                        Spray_i18nBundleDef.get(SprayFailedInitExecutorException.class, message_key),
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
