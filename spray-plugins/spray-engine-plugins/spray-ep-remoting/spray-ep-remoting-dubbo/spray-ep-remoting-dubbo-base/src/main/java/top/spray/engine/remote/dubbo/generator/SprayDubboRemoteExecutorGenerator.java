package top.spray.engine.remote.dubbo.generator;

import cn.hutool.core.lang.Pair;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.ServiceConfig;
import top.spray.core.util.SprayClassLoader;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.remote.dubbo.api.SprayDubboReference;
import top.spray.engine.remote.dubbo.constants.SprayDubboConfigConst;
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
        ReferenceConfig<SprayDubboReference> referenceConfig = new ReferenceConfig<>();
        SprayDubboReference sprayDubboReference = referenceConfig.get();
        boolean isSuccess = sprayDubboReference.generateExecutor(executorMeta.transactionId(), executorMeta.getExecutorNameKey(coordinatorMeta),
                JSON.toJSONString(coordinatorMeta), JSON.toJSONString(executorMeta));
        if (!isSuccess) {
            throw new RuntimeException("");
        }
        return getRemoteExecutor(coordinatorMeta, executorMeta, sprayClassLoader);
    }

}
