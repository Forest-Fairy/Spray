package top.spray.engine.adapter.coordinator.nio;

import top.spray.engine.adapter.coordinator.SprayProcessCoordinatorAdapter;
import top.spray.core.nio.decode.SprayNioDecoder;
import top.spray.core.nio.encode.SprayNioEncoder;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;

public interface SprayProcessCoordinatorNioAdapter extends SprayProcessCoordinatorAdapter {
    SprayNioDecoder getDecoder();
    SprayNioEncoder getEncoder();

    @Override
    SprayProcessCoordinator getActualCoordinator();


}
