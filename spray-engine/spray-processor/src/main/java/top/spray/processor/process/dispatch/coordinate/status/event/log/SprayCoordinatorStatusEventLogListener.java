package top.spray.processor.process.dispatch.coordinate.status.event.log;

import top.spray.core.system.logger.SprayLogger;
import top.spray.core.global.stream.SprayOptional;
import top.spray.processor.infrustructure.listen.SprayListenEventReceiveResult;
import top.spray.processor.infrustructure.listen.SprayListenable;
import top.spray.processor.infrustructure.listen.SprayListenEvent;
import top.spray.processor.i18n.SprayCoordinator_i18n;
import top.spray.processor.infrustructure.listen.SprayListener;
import top.spray.processor.process.dispatch.coordinate.status.SprayCoordinatorStatus;
import top.spray.processor.process.dispatch.coordinate.status.SprayCoordinatorStatusInstance;
import top.spray.processor.process.dispatch.coordinate.status.event.SprayCoordinatorStatusEvent;

public class SprayCoordinatorStatusEventLogListener extends SprayCoordinator_i18n implements SprayListener {
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
        if (source.get() instanceof SprayCoordinatorStatus oldStatus) {
            LOGGER.info("coordinator.status.change",
                    logEvent.getStatusInstance().coordinator().name(),
                    oldStatus.typeName(), logEvent.getStatusInstance().getStatus().getDescription());
            return SprayListenEventReceiveResult.Forward(eventId, receiveTime, "coordinator.status.change");
        }
        return SprayListenEventReceiveResult.NotInterested(eventId, receiveTime);
    }



}
