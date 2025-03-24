package top.spray.engine.process.processor.builder;


import top.spray.engine.process.processor.dispatch.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.process.processor.execute.step.meta.SprayProcessExecuteStepMeta;

public class SprayProcessBuilder {

    private final SprayProcessCoordinatorMeta coordinatorMeta;

    public SprayProcessBuilder(SprayProcessCoordinatorMeta coordinatorMeta) {
        this.coordinatorMeta = coordinatorMeta;
    }

    public SprayProcessBuilder computeWith(SprayComputer computer) {

    }


    private static class SprayStepDescriptor {
        private SprayProcessExecuteStepMeta stepMeta;
        private SprayStepDescriptor(SprayComputer computer) {
            this.stepMeta = stepMeta;
        }
    }

}
