package top.spray.processor.process.dispatch.coordinate.status;

import top.spray.core.global.prop.SprayUnsupportedOperation;
import top.spray.common.tools.SprayOptional;
import top.spray.processor.infrustructure.analyse.SprayAnalysable;
import top.spray.core.system.type.SprayType;
import top.spray.core.system.type.SprayTypeHolder;
import top.spray.processor.infrustructure.analyse.SprayAnalyseResult;
import top.spray.processor.infrustructure.analyse.SprayAnalyser;
import top.spray.processor.infrustructure.listen.SprayListenable;
import top.spray.processor.infrustructure.listen.SprayListener;
import top.spray.processor.process.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.processor.process.dispatch.coordinate.status.event.SprayCoordinatorStatusEvent;


import java.util.*;
import java.util.concurrent.atomic.LongAdder;

public class SprayCoordinatorStatusInstance implements SprayAnalysable, SprayListenable {
    private final SprayProcessCoordinator coordinator;
    private LongAdder runningCounter;
    private SprayTypeHolder<SprayCoordinatorStatus> coordinatorStatus;
    private long startTime;
    private long endTime;
    private List<SprayListener> listeners;

    public SprayCoordinatorStatusInstance(SprayProcessCoordinator coordinator) {
        this.coordinator = coordinator;
        init();
    }

    private void init() {
        this.coordinatorStatus = SprayType.holder(SprayCoordinatorStatus.RUNNING);
        this.runningCounter = new LongAdder();
        this.startTime = System.currentTimeMillis();
        this.listeners = new LinkedList<>(this.coordinator.listListeners().stream().filter(listener -> listener.isForListenable(this)).toList());
    }
    public String transactionId() {
        return this.coordinator.transactionId();
    }

    public SprayProcessCoordinator coordinator() {
        return this.coordinator;
    }



    public long runningExecutorCount() {
        return this.runningCounter.sum();
    }
    public void incrementConsumingCount() {
        this.runningCounter.increment();
    }
    public void decrementConsumingCount() {
        this.runningCounter.decrement();
    }


    public void setStatus(SprayCoordinatorStatus status) {
        if (status.getCode() == this.coordinatorStatus.getCode()) {
            // do nothing if status code is same
            return ;
        }
        SprayCoordinatorStatus oldStatus = SprayCoordinatorStatus.get(this.coordinatorStatus.getCode());
        this.coordinatorStatus.set(status);
        sendChangeEvent(oldStatus);
        if (status.isEnd()) {
            setEnd();
        }
    }

    public SprayCoordinatorStatus getStatus() {
        return SprayCoordinatorStatus.get(this.coordinatorStatus.getCode());
    }

    private void sendChangeEvent(SprayCoordinatorStatus oldStatus) {
        this.listeners.forEach(listener -> listener.receive(
                new SprayCoordinatorStatusChangeEvent( , this, oldStatus)));
    }

    private void setEnd() {
        this.endTime = System.currentTimeMillis();
        this.listeners.forEach(listener -> listener.whenListenableShutdown(this));
    }


    public long getStartTime() {
        return this.startTime;
    }
    public long getEndTime() {
        return this.endTime;
    }
    public long getDuration() {
        return this.endTime == 0 ?
                System.currentTimeMillis() - this.startTime :
                this.endTime - this.startTime;
    }

    @Override
    public <Result extends SprayAnalyseResult> List<Result> listAnalysed(String analyserName) {

    }

    @Override
    public <Result extends SprayAnalyseResult, Analyser extends SprayAnalyser<Result, ?>> List<Result> listAnalysed(Analyser analyser) {
        return ;
    }

    @Override
    public List<SprayListener> listListeners() {
        return Collections.unmodifiableList(this.listeners);
    }

    @Override
    public boolean listenerRegister(SprayListener listener) {
        if (listener.isForListenable(this)) {
            this.listeners.add(listener);
            return true;
        }
        return false;
    }

    public static class SprayCoordinatorStatusChangeEvent implements SprayCoordinatorStatusEvent<SprayCoordinatorStatus> {
        private final SprayCoordinatorStatusInstance instance;
        private final String eventId;
        private final long eventTime;
        private final SprayCoordinatorStatus oldStatus;
        public SprayCoordinatorStatusChangeEvent(String eventId, SprayCoordinatorStatusInstance instance, SprayCoordinatorStatus oldStatus) {
            this.instance = instance;
            this.eventId = eventId;
            this.eventTime = System.currentTimeMillis();
            this.oldStatus = oldStatus;
        }

        @Override
        public SprayCoordinatorStatusInstance getStatusInstance() {
            return instance;
        }

        @Override
        public String getEventId() {
            return eventId;
        }

        @Override
        public long getEventTime() {
            return eventTime;
        }

        @Override
        public void setAttr(String key, String val) {
            SprayUnsupportedOperation.unsupported();
        }

        @Override
        public String getAttr(String key) {
            return SprayUnsupportedOperation.unsupported();
        }

        @Override
        public SprayOptional<SprayCoordinatorStatus> getEventSource() {
            return SprayOptional.of(oldStatus);
        }
    }
}
