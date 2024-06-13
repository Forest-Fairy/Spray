package top.spray.engine.step.executor.storage;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

public interface SprayFileStorageSupportExecutor extends SprayProcessStepExecutor {
    String key_storage_threshold = "storage.file.threshold";
    long getCurrentDataCount();
    default boolean timeToStorageInFile(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        Long storageThreshold = this.getMeta().getLong(key_storage_threshold, 0L);
        if (storageThreshold == -1) {
            return false;
        }
        return storageThreshold < getCurrentDataCount();
    }

    void storageInFile(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);
}
