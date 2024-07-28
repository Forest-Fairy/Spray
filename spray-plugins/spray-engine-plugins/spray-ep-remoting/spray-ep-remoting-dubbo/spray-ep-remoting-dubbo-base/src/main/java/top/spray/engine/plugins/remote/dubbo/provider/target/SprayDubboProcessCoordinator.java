package top.spray.engine.plugins.remote.dubbo.provider.target;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.RpcContext;
import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.types.coordinate.status.SprayCoordinatorStatus;
import top.spray.core.engine.types.data.dispatch.result.SprayDataDispatchResultStatus;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.plugins.remote.dubbo.api.target.SprayDubboVariablesContainer;
import top.spray.engine.plugins.remote.dubbo.constants.SprayDubboProtocolConst;
import top.spray.engine.plugins.remote.dubbo.consumer.target.SprayDubboVariablesContainerImpl;
import top.spray.engine.plugins.remote.dubbo.provider.SprayBaseService;
import top.spray.engine.plugins.remote.dubbo.util.SprayDubboDataUtil;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.plugins.remote.dubbo.api.target.SprayDubboCoordinator;
import top.spray.engine.plugins.remote.dubbo.api.source.reference.SprayDubboCoordinatorReference;
import top.spray.engine.step.condition.SprayNextStepFilter;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SprayDubboProcessCoordinator implements SprayDubboCoordinator {
    private final String url;
    private final String protocol;
    private final SprayBaseService baseService;
    private final SprayProcessCoordinatorMeta coordinatorMeta;
    private final Map<String, SprayProcessStepExecutor> realExecutors;
    private final Map<String, SprayDubboVariablesContainer> variablesContainerMap;
    private final ClassLoader creatorThreadClassLoader;

    public SprayDubboProcessCoordinator(
            SprayProcessCoordinatorMeta coordinatorMeta) {
        this.url = StringUtils.isNotBlank(coordinatorMeta.url()) ? coordinatorMeta.url() :
                RpcContext.getServerContext().getUrl().getAddress();
        SprayDubboProtocolConst p = SprayDubboProtocolConst.parseWithUrl(this.url);
        this.protocol = p == null ? SprayDubboProtocolConst.DUBBO.getValue() : p.getValue();
        this.coordinatorMeta = coordinatorMeta;
        this.baseService = SprayDubboBaseService.get(this);
        this.realExecutors = new ConcurrentHashMap<>(1);
        this.variablesContainerMap = new ConcurrentHashMap<>(1);
        this.creatorThreadClassLoader = Thread.currentThread().getContextClassLoader();
    }

    @Override
    public String url() {
        return this.url;
    }

    @Override
    public String protocol() {
        return this.protocol;
    }

    @Override
    public SprayDubboCoordinatorReference getCoordinatorReference() {
        return baseService;
    }

    @Override
    public SprayProcessCoordinatorMeta getMeta() {
        return coordinatorMeta;
    }

    @Override
    public SprayCoordinatorStatus get() {
        throw new SprayDubboOperationNotSupportException();
    }

    @Override
    public void run() {
        throw new SprayDubboOperationNotSupportException();
    }

    @Override
    public SprayCoordinatorStatus status() {
        return SprayCoordinatorStatus.get(coordinatorReference.getCoordinatorStatus(this.coordinatorMeta.transactionId()));
    }

    @Override
    public String getExecutorNameKey(SprayProcessStepExecutor executor) {
        return executor.getMeta().getExecutorNameKey(this.coordinatorMeta);
    }

    @Override
    public ClassLoader getCreatorThreadClassLoader() {
        return this.creatorThreadClassLoader;
    }

    @Override
    public SprayVariableContainer getVariablesContainer(String identityDataKey) {
        SprayDubboVariablesContainer container = this.variablesContainerMap.get(identityDataKey);
        if (container == null) {
            synchronized (this.variablesContainerMap) {
                if ((container = this.variablesContainerMap.get(identityDataKey)) == null) {
                    SprayData sprayData = baseService.baseInfo(
                            this.coordinatorMeta.transactionId(), identityDataKey);
                    String creator = sprayData.getString("creator");
                    long createTime = Long.parseLong(sprayData.getString("createTime"));
                    this.variablesContainerMap.put(identityDataKey,
                            container = new SprayDubboVariablesContainerImpl(baseService,
                                    this.coordinatorMeta.transactionId(), identityDataKey,
                                    creator, createTime));
                }
            }
        }
        return container;
    }


    @Override
    public int createExecutorCount() {
        return baseService.createExecutorCount(this.coordinatorMeta.transactionId());
    }

    @Override
    public void dispatch(String identityDataKey, SprayNextStepFilter stepFilter, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        String filteredNameKeys = stepFilter == null ? null :
                fromExecutor.getMeta().nextNodes().stream()
                        .filter(nodeMeta ->
                                !stepFilter.executableForNext(fromExecutor, data, still, nodeMeta))
                        .map(nodeMeta -> nodeMeta.getExecutorNameKey(this.getMeta()))
                        .collect(Collectors.joining(","));

        baseService.dispatch(this.coordinatorMeta.transactionId(), filteredNameKeys,
                identityDataKey, fromExecutor.getExecutorNameKey(),
                SprayDubboDataUtil.wrapData(this.getMeta(), fromExecutor.getMeta(), data), still);
    }

    @Override
    public List<SprayDataDispatchResultStatus> getDispatchResults(String dataKey) {
        return null;
    }

    @Override
    public void executeNext(SprayProcessStepExecutor nextStepExecutor, SprayVariableContainer lastVariables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {

    }

    @Override
    public Map<String, SprayProcessStepExecutor> getCachedExecutorMap() {
        return null;
    }

    @Override
    public Set<String> getInputDataKeys(String executorNameKey) {
        return null;
    }

    @Override
    public Set<String> getOutputDataKeys(String executorNameKey) {
        return null;
    }

    @Override
    public void registerExecutor(String executorNameKey, SprayProcessStepExecutor executor) {
        this.realExecutors.put(executorNameKey, executor);
    }

    @Override
    public SprayProcessStepExecutor getStepExecutor(String executorNameKey) {
        return this.realExecutors.get(executorNameKey);
    }

    @Override
    public void closeInRuntime() {

    }
}
