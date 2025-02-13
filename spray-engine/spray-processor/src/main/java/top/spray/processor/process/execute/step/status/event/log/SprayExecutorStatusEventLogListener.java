package top.spray.processor.process.execute.step.status.event.log;

import top.spray.core.system.logger.SprayLogger;
import top.spray.processor.i18n.SprayExecutor_i18n;
import top.spray.processor.infrustructure.listen.SprayListenEvent;
import top.spray.processor.infrustructure.listen.SprayListenEventReceiveResult;
import top.spray.processor.infrustructure.listen.SprayListenable;
import top.spray.processor.infrustructure.listen.SprayListener;
import top.spray.processor.process.execute.step.status.SprayStepStatus;
import top.spray.processor.process.execute.step.status.SprayStepStatusInstance;
import top.spray.processor.process.execute.step.status.event.SprayExecutorStatusEvent;

public class SprayExecutorStatusEventLogListener extends SprayExecutor_i18n implements SprayListener {
    private static final SprayLogger LOGGER = SprayLogger.create(SprayExecutorStatusEventLogListener.class);

    @Override
    public boolean isForListenable(SprayListenable listenable) {
        return listenable instanceof SprayStepStatusInstance;
    }

    @Override
    public SprayListenEventReceiveResult receive(SprayListenEvent<?> event) {
        String eventId = event.getEventId();
        long receiveTime = System.currentTimeMillis();
        if (! (event instanceof SprayExecutorStatusEvent<?> logEvent) || event.getEventSource().isNotPresent()) {
            return SprayListenEventReceiveResult.NotInterested(eventId, receiveTime);
        }
//        event.getEventSource()
        SprayStepStatusInstance statusInstance = logEvent.getStatusInstance();
        Object source = logEvent.getEventSource().orElse(null);
        if (source instanceof SprayStepStatus oldStatus) {
            LOGGER.info("step.status.change",
                    statusInstance.getCoordinator().name(),
                    statusInstance.getExecutorFacade().executorNameKey(),
                    oldStatus.typeName(), statusInstance.getStatus().typeName());
            return SprayListenEventReceiveResult.Success(eventId, receiveTime, "step.status.change");
        }
        return SprayListenEventReceiveResult.NotInterested(eventId, receiveTime);
    }

}
