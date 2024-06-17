package top.spray.engine.factory;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.mutable.Mutable;
import cn.hutool.core.lang.mutable.MutableObj;
import top.spray.core.engine.props.SprayData;
import top.spray.core.util.ServiceUtil;
import top.spray.core.util.SprayClassLoader;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.handler.record.SprayRecordStrategyBaseHandler;
import top.spray.engine.step.handler.record.SprayRecordStrategyHandler;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SprayRecordStrategyHandlerFactory {
    public static SprayRecordStrategyHandler create(SprayProcessStepExecutor stepExecutor, String recordType,
                                                    SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        for (SprayRecordStrategyHandler sprayRecordStrategyHandler :
                ServiceUtil.loadServiceClassNameMapCache(SprayRecordStrategyHandler.class).values()) {
            if (sprayRecordStrategyHandler.canHandle(stepExecutor, recordType, fromExecutor, data, still)) {
                sprayRecordStrategyHandler.record(stepExecutor, recordType, fromExecutor, data, still);
                return sprayRecordStrategyHandler;
            }
        }
        return null;
    }
}
