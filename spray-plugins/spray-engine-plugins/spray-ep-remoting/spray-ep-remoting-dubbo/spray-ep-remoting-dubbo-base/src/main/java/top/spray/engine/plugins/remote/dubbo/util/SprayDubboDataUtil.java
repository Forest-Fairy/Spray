package top.spray.engine.plugins.remote.dubbo.util;

import top.spray.core.engine.props.SprayData;
import top.spray.core.util.SprayUtf8s;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.step.meta.SprayProcessStepMeta;

public class SprayDubboDataUtil {
    /**
     * wrapData data by config
     * @param data data
     * @return data bytes
     */
    public static byte[] wrapData(SprayProcessCoordinatorMeta coordinatorMeta,
                                  SprayProcessStepMeta stepMeta,
                                  SprayData data) {
        // TODO pressing data and handle with crypto
        return data.toJson().getBytes(SprayUtf8s.Charset);
    }

    /**
     * resolveData data by config
     * @param bytes data bytes
     * @return data
     */
    public static SprayData resolveData(SprayProcessCoordinatorMeta coordinatorMeta,
                                        SprayProcessStepMeta stepMeta,
                                        byte[] bytes) {
        return SprayData.fromJson(new String(bytes, SprayUtf8s.Charset));
    }

}
