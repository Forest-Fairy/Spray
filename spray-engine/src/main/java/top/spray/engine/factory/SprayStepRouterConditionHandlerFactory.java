package top.spray.engine.factory;

import top.spray.engine.step.handler.router.SprayStepRouterConditionHandler;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.List;

public interface SprayStepRouterConditionHandlerFactory {
    static SprayStepRouterConditionHandler create(SprayProcessStepMeta currentStepMeta) {
        // [{'type': 'factoryBean', 'className': 'xxx'},
        //  {type: 'javaCode', 'code': 'xxx'},
        //  {'type': 'pyCode', 'code': 'xxx'}]
        currentStepMeta.getString("router.conditions", "[]")
    }
    List<SprayStepRouterConditionHandler> get();
}
