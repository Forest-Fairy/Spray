package top.spray.engine.process.processor.execute.step.executor.facade;

import top.spray.common.tools.SprayExceptionUtil;
import top.spray.common.tools.SprayOptional;
import top.spray.core.dynamic.SprayClassLoader;
import top.spray.core.thread.SprayPoolExecutor;
import top.spray.engine.process.infrastructure.prop.SprayVariableContainer;
import top.spray.engine.process.processor.execute.exception.SprayStepExecuteError;
import top.spray.engine.process.infrastructure.analyse.SprayAnalyseResult;
import top.spray.engine.process.infrastructure.analyse.SprayAnalyser;
import top.spray.engine.process.infrastructure.listen.SprayListener;
import top.spray.engine.process.processor.execute.step.executor.SprayStepExecutor;
import top.spray.engine.process.processor.execute.step.meta.SprayExecutorType;
import top.spray.engine.process.processor.execute.step.meta.SprayOptionalData;
import top.spray.engine.process.processor.execute.step.meta.SprayProcessExecuteStepMeta;
import top.spray.engine.process.thread.SprayPoolExecutorBuilder;
import top.spray.engine.process.processor.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.process.processor.execute.step.status.SprayStepStatusInstance;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Define the executor of a process node
 */
public class SprayDefaultStepExecutorFacade implements SprayStepFacade, SprayStepExecutorOwner {
    private SprayProcessExecuteStepMeta stepMeta;
    private SprayProcessCoordinator coordinator;
    private SprayClassLoader classLoader;
    private SprayPoolExecutor threadPool;
    private SprayStepStatusInstance stepResult;
    private List<SprayListener> unmodifiableListeners;
    private List<SprayListener> listeners;
    private SprayStepExecutor executor;
    private Map<String, Object> executeInfo;

    public SprayDefaultStepExecutorFacade(SprayProcessExecuteStepMeta stepMeta, SprayProcessCoordinator coordinator, SprayClassLoader classLoader) {
        this.coordinator = coordinator;
        this.stepMeta = stepMeta;
        this.classLoader = classLoader;
        this.executor = ;
    }

    public final synchronized void initOnlyAtCreate() {
        if (stepResult != null) {
            return;
        }
        this.stepResult = new SprayStepStatusInstance(this.coordinator, this);
        this.listeners = new LinkedList<>();
        this.unmodifiableListeners = Collections.unmodifiableList(listeners);
        if (this.stepMeta.isAsync() && this.getCoordinator().getMeta().asyncSupport()) {
            this.threadPool = SprayPoolExecutorBuilder.newBuilder(classLoader)
                    .buildWithMeta(stepMeta)
                    .build();
        } else {
            this.threadPool = null;
        }
        this.executeInfo = new ConcurrentHashMap<>(15);
        this.initOnlyAtCreate0();
    }
    protected void initOnlyAtCreate0() {}


    @Override
    public final SprayProcessExecuteStepMeta getMeta() {
        return this.stepMeta;
    }
    @Override
    public final SprayProcessCoordinator getCoordinator() {
        return this.coordinator;
    }
    @Override
    public final SprayClassLoader getClassLoader() {
        return this.classLoader;
    }

    @Override
    public final SprayStepStatusInstance runningStatus() {
        return this.stepResult;
    }

    @Override
    public SprayExecutorType getExecutorType() {
        return executor.executorType();
    }


    @Override
    public final void receive(
            @Nonnull String variableContainerIdentityDataKey,
            @Nullable String fromExecutorNameKey,
            @Nullable SprayOptionalData optionalData) {
        if (executable(variableContainerIdentityDataKey, fromExecutorNameKey, optionalData)) {
            doExecute(variableContainerIdentityDataKey, fromExecutorNameKey, optionalData);
        }
    }

    private boolean executable(String variableContainerIdentityDataKey, String fromExecutorNameKey, SprayOptionalData optionalData) {
        return this.executor.beforeExecute(variableContainerIdentityDataKey, fromExecutorNameKey, optionalData) != null;
    }

    private void doExecute(String variableContainerIdentityDataKey, String fromExecutorNameKey, SprayOptionalData optionalData) {
        if (this.threadPool != null) {
            this.threadPool.submit(() -> {
                doExecute0(variableContainerIdentityDataKey, fromExecutorNameKey, optionalData);
            });
        } else {
            doExecute0(variableContainerIdentityDataKey, fromExecutorNameKey, optionalData);
        }
    }

    private void doExecute0(String variableContainerIdentityDataKey, String fromExecutorNameKey, SprayOptionalData optionalData) {
        try {
            Map<String, Object> newInfo = this.executor.execute(variableContainerIdentityDataKey, fromExecutorNameKey, optionalData);
            SprayOptional.of(newInfo).ifPresent(info -> {
                this.executor.infoMerge(executeInfo, info);
            });
        } catch (Throwable t) {
            Throwable ifErrored = this.executor.ifErrored(variableContainerIdentityDataKey, fromExecutorNameKey, optionalData, t);
            if (ifErrored != null) {
                // error needs to be handled
                this.executeErrored(variableContainerIdentityDataKey, fromExecutorNameKey, optionalData, ifErrored);
                executeInfo.put("isErrored", true);
                executeInfo.put("errorType", ifErrored.getClass().getName());
                executeInfo.put("errorMsg", ifErrored.getMessage());
                executeInfo.put("errorTrace", SprayExceptionUtil.stacktraceToString(ifErrored));
            }
        }
        try {
            this.executor.afterExecute(variableContainerIdentityDataKey, fromExecutorNameKey, optionalData, executeInfo);
        } catch (Throwable t) {
            this.afterExecuteErrored(variableContainerIdentityDataKey, fromExecutorNameKey, optionalData, t);
        }
    }


    protected void executeErrored(String variableContainerIdentityDataKey, String fromExecutorNameKey, SprayOptionalData optionalData, Throwable t) {
        // TODO handle exception with error handle strategy
        // if current step has no any handle strategy, then send error to coordinator
        throw new SprayStepExecuteError(this, t);
    }

    protected void afterExecuteErrored(String variableContainerIdentityDataKey, String fromExecutorNameKey, SprayOptionalData optionalData, Throwable t) {
        // TODO handle exception with error handle strategy
        throw new SprayStepExecuteError(this, t);
    }

    @Override
    public void shutdown() {
        if (this.threadPool != null) {
            this.threadPool.shutdown();
        }
    }

    @Override
    public void forceShutdown() {
        if (this.threadPool != null) {
            this.threadPool.shutdownNow();
        }
    }

    @Override
    public <Result extends SprayAnalyseResult> List<Result> listAnalysed(String analyserName) {
        return executor.listAnalysed(analyserName);
    }

    @Override
    public <Result extends SprayAnalyseResult, Analyser extends SprayAnalyser<Result, ?>> List<Result> listAnalysed(Analyser analyser) {
        return executor.listAnalysed(analyser);
    }

    @Override
    public List<SprayListener> listListeners() {
        return Collections.unmodifiableList(listeners);
    }

    @Override
    public synchronized boolean listenerRegister(SprayListener listener) {
        if (listener.isForListenable(this) && !listeners.contains(listener)) {
            listeners.add(listener);
            return true;
        }
        return false;
    }

    @Override
    public String getExecutorNameKey() {
        return this.executorNameKey();
    }

    @Override
    public SprayProcessExecuteStepMeta getStepMeta() {
        return this.getMeta();
    }

    @Override
    public List<? extends SprayProcessExecuteStepMeta> listNextSteps() {
        return this.getCoordinator().listNextSteps(this.executor.executorNameKey());
    }

    @Override
    public String getExecutorNameKey(SprayProcessExecuteStepMeta nextStepMeta) {
        return this.coordinator.getExecutorNameKey(nextStepMeta);
    }

    @Override
    public void dispatchData(String variableContainerIdentityDataKey, SprayStepExecutor sprayStepExecutor, SprayOptionalData optionalData, String toExecutorNameKeys) {
        this.coordinator.dispatchData(variableContainerIdentityDataKey, sprayStepExecutor, optionalData, toExecutorNameKeys);
    }

    @Override
    public SprayStepFacade getExecutorFacade(String executorNameKey) {
        return this.coordinator.getExecutorFacade(executorNameKey);
    }

    @Override
    public SprayVariableContainer getVariableContainer(String variableContainerIdentityDataKey) {
        return this.coordinator.getVariableManager().getVariableContainer(variableContainerIdentityDataKey);
    }
}
