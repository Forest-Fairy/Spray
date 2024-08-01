package top.spray.engine.plugins.remote.dubbo.consumer.target;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.RpcContext;
import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.types.coordinate.status.SprayCoordinatorStatus;
import top.spray.core.engine.types.data.dispatch.result.SprayEventDispatchResultStatus;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.plugins.remote.dubbo.api.target.SprayDubboBaseService;
import top.spray.engine.plugins.remote.dubbo.api.target.SprayDubboVariablesContainer;
import top.spray.engine.plugins.remote.dubbo.api.target.reference.SprayDubboExecutorReference;
import top.spray.engine.plugins.remote.dubbo.constants.SprayDubboProtocolConst;
import top.spray.engine.plugins.remote.dubbo.util.SprayDubboDataUtil;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.plugins.remote.dubbo.api.target.SprayDubboCoordinator;
import top.spray.engine.plugins.remote.dubbo.api.source.reference.SprayDubboCoordinatorReference;
import top.spray.engine.step.condition.SprayNextStepFilter;
import top.spray.engine.step.executor.SprayExecutorDefinition;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SprayDubboCoordinatorImpl implements SprayDubboCoordinator {
    private final String url;
    private final String protocol;
    private final SprayDubboBaseService baseService;
    private final SprayProcessCoordinatorMeta coordinatorMeta;
    private final Map<String, SprayProcessStepMeta> stepMetas;
    private final Map<String, SprayExecutorDefinition> executors;
    private final Map<String, SprayDubboVariablesContainer> variablesContainerMap;
    private final ClassLoader creatorThreadClassLoader;

    public SprayDubboCoordinatorImpl(
            SprayProcessCoordinatorMeta coordinatorMeta, int port) {
        if (StringUtils.isNotBlank(coordinatorMeta.url())) {
            this.url = coordinatorMeta.url();
            this.protocol = SprayDubboProtocolConst.parseWithUrl(this.url).getValue();
        } else {
            String host = RpcContext.getServerContext().getUrl().getHost();
            this.url = String.format("%s://%s:%d", SprayDubboProtocolConst.DUBBO.getValue(), host, port);
            this.protocol = SprayDubboProtocolConst.DUBBO.getValue();
        }
        this.coordinatorMeta = coordinatorMeta;
        this.baseService = SprayDubboBaseService.get(this);
        this.stepMetas = new ConcurrentHashMap<>(1);
        this.executors = new ConcurrentHashMap<>(1);
        this.variablesContainerMap = new ConcurrentHashMap<>(1);
        this.creatorThreadClassLoader = Thread.currentThread().getContextClassLoader();
        initMeta();
    }

    private void initMeta() {
        recordMeta(this.coordinatorMeta.getStartNodes());
    }

    private void recordMeta(List<SprayProcessStepMeta> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return;
        }
        nodes.forEach(nodeMeta -> {
            if (this.stepMetas.put(nodeMeta.getExecutorNameKey(this.coordinatorMeta), nodeMeta) == null) {
                // put next if it is first time to be recorded
                recordMeta(nodeMeta.nextNodes());
            }
        });
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
        return SprayCoordinatorStatus.get(baseService.getCoordinatorStatus(this.coordinatorMeta.transactionId()));
    }

    @Override
    public String getExecutorNameKey(SprayExecutorDefinition executor) {
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
    public int executorCount() {
        return baseService.createExecutorCount(this.coordinatorMeta.transactionId());
    }

    @Override
    public void publishData(String identityDataKey, SprayNextStepFilter stepFilter, SprayExecutorDefinition fromExecutor, SprayData data, boolean still) {
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
    public List<SprayEventDispatchResultStatus> getDispatchResults(String dataKey) {
        throw new SprayDubboOperationNotSupportException();
    }

    @Override
    public void sendDataPublishEvent(SprayExecutorDefinition nextStepExecutor, SprayVariableContainer lastVariables, SprayExecutorDefinition fromExecutor, SprayData data, boolean still) {
        throw new SprayDubboOperationNotSupportException();
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
    public void registerExecutor(String executorNameKey, SprayExecutorDefinition executor) {
        this.executors.put(executorNameKey, executor);
    }

    @Override
    public SprayExecutorDefinition getStepExecutor(String executorNameKey) {
        SprayExecutorDefinition executor = this.executors.get(executorNameKey);
        if (executor == null) {
            synchronized (this.executors) {
                if ((executor = this.executors.get(executorNameKey)) == null) {
                    if (this.baseService.ensureProviderCreated(this.coordinatorMeta.transactionId(), executorNameKey)) {
                        executor = new SprayDubboSrcExecutorFacade(
                                SprayDubboExecutorReference.createSrcReference(
                                        coordinatorMeta, this.stepMetas.get(executorNameKey)))
                    }
                }
            }
        }
        return executor;
    }

    @Override
    public void closeInRuntime() {

    }
}
