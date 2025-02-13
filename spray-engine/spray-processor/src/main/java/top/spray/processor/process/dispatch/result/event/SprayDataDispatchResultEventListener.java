//package top.spray.engine.process.dispatch.result.event;
//
//import top.spray.processor.process.dispatch.event.SprayDataDispatchEventListener;
//import top.spray.processor.infrustructure.listen.SprayEventType;
//import top.spray.processor.process.data.event.impl.SprayDataDispatchResultType;
//
//public interface SprayDataDispatchResultEventListener extends SprayDataDispatchEventListener<SprayDataDispatchResultEvent> {
//    @Override
//    default boolean support(SprayEventType eventType) {
//        if (eventType instanceof SprayDataDispatchResultType dispatchEventType) {
//            return support0(dispatchEventType);
//        } else {
//            return false;
//        }
//    }
//
//    boolean support0(SprayDataDispatchResultType dispatchEventType);
//}
