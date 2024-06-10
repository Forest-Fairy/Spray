package top.spray.engine.coordinate.coordinator;

import top.spray.engine.base.execute.SprayListenable;
import top.spray.engine.base.result.SprayCoordinateResult;
import top.spray.engine.step.executor.SprayExecutorListener;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SprayMetaDriveProcessCoordinator implements
        SprayProcessCoordinator,
        SprayListenable<SprayExecutorListener> {

    private final SprayProcessCoordinatorMeta coordinatorMeta;
    private final List<SprayExecutorListener> listeners;


    public SprayMetaDriveProcessCoordinator(SprayProcessCoordinatorMeta coordinatorMeta) {
        this.coordinatorMeta = coordinatorMeta;
        this.listeners = new ArrayList<>();
    }

    @Override
    public SprayProcessCoordinatorMeta getMeta() {
        return coordinatorMeta;
    }


    @Override
    public SprayMetaDriveProcessCoordinator addListener(SprayExecutorListener... listeners) {
        this.listeners.addAll(Arrays.asList(listeners));
        return this;
    }

    @Override
    public List<SprayExecutorListener> getListeners() {
        return this.listeners;
    }


    @Override
    public SprayCoordinateResult execute() {

        return null;
    }

}
