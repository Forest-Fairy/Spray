//package top.spray.engine.process.dispatch.coordinate.status.event;
//
//import top.spray.core.factory.SprayFactory;
//import top.spray.core.factory.SprayMaterial;
//import top.spray.processor.process.dispatch.coordinate.status.SprayCoordinatorStatusInstance;
//
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//public interface SprayCoordinatorStatusEventListenerFactory extends SprayFactory<SprayCoordinatorStatusEventListener> {
//    static List<SprayCoordinatorStatusEventListener> getListeners(SprayCoordinatorStatusInstance instance) {
//        List<SprayCoordinatorStatusEventListener> listeners = new LinkedList<>();
//        Map<String, SprayCoordinatorStatusEventListenerFactory> factories = Container.getRegisteredFactories(SprayCoordinatorStatusEventListenerFactory.class);
//        Map<String, Object> props = new HashMap<>();
//        props.put(SprayCoordinatorStatusInstance.class.getName(), instance);
//        SprayMaterial material = new SprayMaterial(props);
//        for (SprayCoordinatorStatusEventListenerFactory factory : factories.values()) {
//            if (factory.support(material)) {
//                listeners.add(factory.produce(material));
//            }
//        }
//        return listeners;
//    }
//}
