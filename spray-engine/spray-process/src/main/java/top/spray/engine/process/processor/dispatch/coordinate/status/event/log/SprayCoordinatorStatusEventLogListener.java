package top.spray.engine.process.processor.dispatch.coordinate.status.event.log;

import top.spray.core.i18n.SprayResourceBundle;
import top.spray.core.logger.SprayLogger;
import top.spray.common.tools.SprayOptional;
import top.spray.engine.i18n.SprayEngineBundleDef;
import top.spray.engine.process.infrastructure.listen.SprayListenEventReceiveResult;
import top.spray.engine.process.infrastructure.listen.SprayListenable;
import top.spray.engine.process.infrastructure.listen.SprayListenEvent;
import top.spray.engine.process.infrastructure.listen.SprayListener;
import top.spray.engine.process.processor.dispatch.coordinate.status.SprayCoordinatorStatus;
import top.spray.engine.process.processor.dispatch.coordinate.status.SprayCoordinatorStatusInstance;
import top.spray.engine.process.processor.dispatch.coordinate.status.event.SprayCoordinatorStatusEvent;

@SprayResourceBundle(SprayEngineBundleDef.BUNDLE_NAME_PREFIX + "coordinate.messages")
public class SprayCoordinatorStatusEventLogListener implements SprayListener {
    private static final SprayLogger LOGGER = SprayLogger.create(SprayCoordinatorStatusEventLogListener.class);


    @Override
    public boolean isForListenable(SprayListenable listenable) {
        return listenable instanceof SprayCoordinatorStatusInstance;
    }

    @Override
    public void whenListenableShutdown(SprayListenable listenable) {

    }

    @Override
    public SprayListenEventReceiveResult receive(SprayListenEvent<?> event) {
        String eventId = event.getEventId();
        long receiveTime = System.currentTimeMillis();
        if (! (event instanceof SprayCoordinatorStatusEvent<?> logEvent) || event.getEventSource().isNotPresent()) {
            return SprayListenEventReceiveResult.NotInterested(eventId, receiveTime);
        }
//        event.getEventSource()
        SprayCoordinatorStatusInstance statusInstance = logEvent.getStatusInstance();
        SprayOptional<?> source = logEvent.getEventSource();
        if (source.orElse(null) instanceof SprayCoordinatorStatus oldStatus) {
            LOGGER.info("coordinator.status.changed",
                    logEvent.getStatusInstance().coordinator().name(),
                    oldStatus.typeName(), logEvent.getStatusInstance().getStatus().getDescription());
            return SprayListenEventReceiveResult.Forward(eventId, receiveTime, "coordinator.status.change");
        }
        return SprayListenEventReceiveResult.NotInterested(eventId, receiveTime);
    }



}
