package top.spray.engine.process.processor.execute.step.status.event.log;

import top.spray.core.i18n.SprayResourceBundle;
import top.spray.core.logger.SprayLogger;
import top.spray.processor.i18n.SprayEngineBundleDef;
import top.spray.processor.infrustructure.listen.SprayListenEvent;
import top.spray.processor.infrustructure.listen.SprayListenEventReceiveResult;
import top.spray.processor.infrustructure.listen.SprayListenable;
import top.spray.processor.infrustructure.listen.SprayListener;
import top.spray.engine.process.processor.execute.step.status.SprayStepStatus;
import top.spray.engine.process.processor.execute.step.status.SprayStepStatusInstance;
import top.spray.engine.process.processor.execute.step.status.event.SprayExecutorStatusEvent;

@SprayResourceBundle(SprayEngineBundleDef.BUNDLE_NAME_PREFIX + "step.messages")
public class SprayExecutorStatusEventLogListener implements SprayListener {
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
            LOGGER.info("step.status.changed",
                    statusInstance.getCoordinator().name(),
                    statusInstance.getExecutorFacade().executorNameKey(),
                    oldStatus.typeName(), statusInstance.getStatus().typeName());
            return SprayListenEventReceiveResult.Success(eventId, receiveTime, "step.status.change");
        }
        return SprayListenEventReceiveResult.NotInterested(eventId, receiveTime);
    }

}
