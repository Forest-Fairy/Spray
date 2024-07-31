package top.spray.engine.step.executor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import top.spray.core.engine.props.SprayData;
import top.spray.core.thread.SprayPoolExecutor;
import top.spray.core.thread.SprayThread;
import top.spray.engine.design.event.annotation.SpraySubscribe;
import top.spray.engine.design.event.constant.SprayEventPassByStrategy;
import top.spray.engine.design.event.model.SprayEvent;
import top.spray.engine.design.event.model.coordinate.process.SprayProcessStartEvent;
import top.spray.engine.design.event.model.coordinate.process.SprayProcessStopEvent;
import top.spray.engine.design.event.util.SprayEvents;
import top.spray.engine.design.event.util.SpraySubscribes;
import top.spray.engine.design.worker.SprayEventWorker;
import top.spray.engine.thread.SprayPoolExecutorBuilder;
import top.spray.core.dynamic.loader.SprayClassLoader;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.exception.SprayExecuteException;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.condition.SprayNextStepFilter;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Define the executor of a process node
 */
public class SprayBaseStepExecutor implements SprayProcessStepExecutor {
    private SprayProcessStepMeta stepMeta;
    private SprayProcessCoordinator coordinator;
    private SprayClassLoader classLoader;
    private SprayStepResultInstance stepResult;
    private long createTime;
    private SprayPoolExecutor pool;
    private SprayEventPassByStrategy eventPassByStrategy;
    private Map<String, List<Method>> subscribedMethods;
    private volatile int cur;
    private volatile int next;
    private SprayEvent[] events;
    private SprayThread consumeThread;
    private Set<String> blackEvents;
    private Set<String> whiteEvents;


    @Override
    public synchronized final boolean initOnlyAtCreate() {
        if (createTime != 0) {
            return false;
        }
        this.stepResult = new SprayStepResultInstance(
                this.getCoordinator().getMeta(),
                this.getMeta(), this.getClassLoader());
        this.createTime = System.currentTimeMillis();
        if (this.stepMeta.isAsync() && this.getCoordinator().getMeta().asyncSupport()) {
            this.pool = new SprayPoolExecutorBuilder(
                    this.stepMeta.coreThreadCount(),
                    this.stepMeta.maxThreadCount(),
                    this.stepMeta.threadAliveTime(),
                    this.stepMeta.threadAliveTimeUnit(),
                    SprayPoolExecutor.newDefaultFactory(),
                    this.stepMeta.queueCapacity()).build();
        } else {
            this.pool = null;
        }
        this.eventPassByStrategy = SprayEventPassByStrategy.StrategyOf(this.getClass());
        this.blackEvents = StringUtils.isBlank(this.getMeta().getBlackEvents()) ? null :
                Set.of(this.getMeta().getBlackEvents().split(","));
        this.whiteEvents = StringUtils.isBlank(this.getMeta().getWhiteEvents()) ? null :
                Set.of(this.getMeta().getWhiteEvents().split(","));
        this.cur = 0;
        this.next = 0;
        this.subscribedMethods = new HashMap<>();
        SpraySubscribes.readSubscribeMethodsOnClass(this.subscribedMethods, this.getClass());
        this.events = new SprayEvent[this.stepMeta.maxConcurrentReceiving()];
        this.initOnlyAtCreate0();
        this.consumeThread = new SprayThread(new SprayEventWorker(this));
        this.consumeThread.start();
        return true;
    }
    protected void initOnlyAtCreate0() {}

    @Override
    public final long runningCount() {
        return this.getStepResult().runningCount();
    }

    @Override
    public final long getCreateTime() {
        return this.createTime;
    }

    @Override
    public final void setMeta(SprayProcessStepMeta meta) {
        this.stepMeta = meta;
    }
    @Override
    public final void setCoordinator(SprayProcessCoordinator coordinator) {
        this.coordinator = coordinator;
    }
    @Override
    public final void setClassLoader(SprayClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    @Override
    public final SprayProcessStepMeta getMeta() {
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

    public final SprayStepResultInstance getStepResult() {
        return this.stepResult;
    }

    @Override
    public int varCopyMode() {
        return this.stepMeta.varCopyMode();
    }


    @Override
    public SprayPoolExecutor getThreadPoll() {
        return this.pool;
    }


    @Override
    public boolean canEventPassBy(SprayEvent event) {
        switch (this.eventPassByStrategy) {
            case NO -> {
                return false;
            }
            case YES -> {
                return true;
            }
            default -> {
                if (this.blackEvents != null) {
                    return !this.blackEvents.contains(event.getEventName());
                } else if (this.whiteEvents != null) {
                    return this.whiteEvents.contains(event.getEventName());
                } else {
                    return true;
                }
            }
        }
    }

    protected final void publishData(SprayVariableContainer variableContainer, SprayData data, boolean still) {
        this.publishData(variableContainer, data, still, null);
    }
    protected final void publishData(SprayVariableContainer variableContainer, SprayData data, boolean still, SprayNextStepFilter stepFilter) {
        this.getCoordinator().publishData(variableContainer.identityDataKey(), stepFilter, this, data, still);
    }

    @Override
    public final void receive(SprayEvent event) {
        if (event == null) {
            // TODO make ex
            throw new IllegalArgumentException("event is null");
        }
        try {
            if (notDone(next)) {
                Thread.yield();
                while (notDone(next)) {
                    Thread.sleep(100);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        synchronized (events[next]) {
            events[next] = event;
            next++;
            if (next > events.length) {
                next = 0;
            }
        }
    }

    private synchronized void initBeforeConsume(SprayEvent event) {
        Thread.currentThread().setContextClassLoader(this.getClassLoader());
        MDC.put("transactionId", this.getCoordinator().getMeta().transactionId());
        MDC.put("executorId", this.stepMeta.getExecutorNameKey(this.getCoordinator().getMeta()));
        MDC.put("tid", String.valueOf(Thread.currentThread().getId()));
        initBeforeRun0(event);
    }
    protected synchronized void initBeforeRun0(SprayEvent event) {}

    @Override
    public final synchronized void consume() throws InterruptedException {
        if (events[cur] == null) {
            // let out cpu
            Thread.yield();
            while (events[cur] == null) {
                Thread.sleep(100);
            }
        }
        try {
            this.getStepResult().incrementConsumingCount();
            this.initBeforeConsume(events[cur]);
            consume(events[cur]);
        } finally {
            this.getStepResult().decrementConsumingCount();
            events[cur] = null;
            cur++;
            if (cur > events.length) {
                cur = 0;
            }
        }
    }

    private void consume(SprayEvent event) {
        if (SprayProcessStartEvent.NAME.equals(event.getEventName()) ||
                SprayProcessStopEvent.NAME.equals(event.getEventTime())) {
            // ignored
            return ;
        }
        List<Method> subscribeMethods = this.subscribedMethods.get(event.getEventName());
        if (subscribeMethods != null) {
            SprayEvents.set(event);
            if (this.getThreadPoll() != null) {
                this.getThreadPoll().execute(() -> {
                    // set inside thread
                    SprayEvents.set(event);
                    invokeEvent(event, subscribeMethods);
                });
            } else {
                invokeEvent(event, subscribeMethods);
            }
        }
    }

    private void invokeEvent(SprayEvent event, List<Method> subscribeMethods) {
        for (Method method : subscribeMethods) {
            try {
                if (method.getParameterTypes().length > 0) {
                    method.invoke(this, event);
                } else {
                    method.invoke(this);
                }
            } catch (Exception e) {
                // TODO handle ex
                this.getCoordinator().handleErrorEvent(
                        this.getMeta(), method, events[cur], e);
            }
        }
    }

    private boolean notDone(int next) {
        return events[next] != null;
    }

    /**
     * blocked until all the before executions done
     */
    protected final void waitUntilBeforeExecutionDone() {
        if (this.runningCount() > 0) {
            Thread.yield();
            while (this.runningCount() > 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new SprayExecuteException(this.getMeta(), e);
                }
            }
        }
    }

    @Override
    @SpraySubscribe(SprayProcessStopEvent.NAME)
    public final void closeInRuntime() {
        waitUntilBeforeExecutionDone();
        try {
            this.getThreadPoll().shutdown();
        } catch (Exception e) {
            // TODO Handle with handler
            throw e;
        }
        this.getClassLoader().closeInRuntime();
        destroy();
    }
    protected void destroy() {}
}
