package top.spray.core.engine.props;

import java.util.Map;
import java.util.concurrent.ThreadFactory;

public interface SprayThreadFactory extends ThreadFactory {

    @Override
    SprayThread newThread(Runnable r);
}
