package top.spray.engine.plugins.remote.dubbo.consumer.source;

import com.alibaba.fastjson2.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.ReferenceConfig;
import top.spray.core.dynamic.loader.SprayClassLoader;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.plugins.remote.dubbo.api.target.reference.SprayDubboExecutorFactoryReference;
import top.spray.engine.plugins.remote.dubbo.constants.SprayDubboConfigConst;
import top.spray.engine.plugins.remote.dubbo.api.source.SprayDubboExecutor;
import top.spray.engine.remoting.SprayRemoteStepExecutor;
import top.spray.engine.remoting.generator.SprayRemoteStepExecutorGenerator;
import top.spray.engine.step.meta.SprayProcessStepMeta;

public class SprayDubboRemoteExecutorGenerator implements SprayRemoteStepExecutorGenerator {
    @Override
    public boolean support(SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessStepMeta executorMeta, SprayClassLoader sprayClassLoader) {
        return executorMeta.isRemoting() &&
                StringUtils.isNotBlank(executorMeta.remotingJarFiles()) &&
                StringUtils.isNotBlank(executorMeta.getString(SprayDubboConfigConst.KEY_URL));
    }

    @Override
    public SprayRemoteStepExecutor generateExecutor(SprayProcessCoordinatorMeta coordinatorMeta,
                                                    SprayProcessStepMeta executorMeta,
                                                    SprayClassLoader sprayClassLoader) {
        ReferenceConfig<SprayDubboExecutorFactoryReference> referenceConfig = new ReferenceConfig<>();
        // TODO build the reference config
        SprayDubboExecutorFactoryReference sprayDubboReference = referenceConfig.get();
        boolean isSuccess = sprayDubboReference.generateExecutor(executorMeta.transactionId(), executorMeta.getExecutorNameKey(coordinatorMeta),
                JSON.toJSONString(coordinatorMeta), JSON.toJSONString(executorMeta));
        if (!isSuccess) {
            throw new RuntimeException("");
        }
        // generate a remote executor for coordinator
        return getRemoteExecutor(coordinatorMeta, executorMeta, sprayClassLoader);
    }

    private SprayDubboExecutor getRemoteExecutor(SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessStepMeta executorMeta, SprayClassLoader sprayClassLoader) {

        return null;
    }

}