package top.spray.engine.step.executor.storage;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

public interface SprayFileStorageSupportExecutor extends SprayProcessStepExecutor {
    String KEY_STORAGE_THRESHOLD = "storage.file.threshold";
    long getCurrentDataCount();
    default boolean timeToStorageInFile(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        Long storageThreshold = this.getMeta().getLong(KEY_STORAGE_THRESHOLD, 0L);
        if (storageThreshold == -1) {
            return false;
        }
        return storageThreshold < getCurrentDataCount();
    }

    void storageInFile(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);
}
