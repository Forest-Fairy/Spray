package top.spray.processor.process.data.event.impl;

import top.spray.common.tools.Sprays;
import top.spray.core.global.config.util.SpraySystemConfigurations;
import top.spray.processor.infrustructure.listen.SprayListenEvent;
import top.spray.processor.infrustructure.listen.SprayListenEventReceiveResult;
import top.spray.processor.infrustructure.listen.SprayListenable;
import top.spray.processor.infrustructure.listen.SprayListener;
import top.spray.processor.process.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.processor.exception.SprayDispatchResultHandleException;
import top.spray.processor.process.execute.step.executor.SprayStepExecutor;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SprayDataDispatchListener_File implements SprayListener {
    public static final SprayDataDispatchListener_File INSTANCE =
            new SprayDataDispatchListener_File();

    private static final Map<String, FileOutputStream> FILE_STREAM_CACHE = new ConcurrentHashMap<>();

    private SprayDataDispatchListener_File() {}

    @Override
    public boolean isForListenable(SprayListenable listenable) {
        return listenable instanceof SprayProcessCoordinator
                || listenable instanceof SprayStepExecutor;
    }

    @Override
    public void whenListenableShutdown(SprayListenable listenable) {
        if (listenable instanceof SprayProcessCoordinator coordinator) {
            File propertiesFile = getResultFile(coordinator.transactionId());
            try {
                getFileStream(propertiesFile).close();
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public SprayListenEventReceiveResult receive(SprayListenEvent<?> event) {
        if (!(event instanceof SprayDataDispatchEvent dataDispatchEvent)) {
            return SprayListenEventReceiveResult.NotInterested(event.getEventId(), System.currentTimeMillis());
        }
        try {
            File propertiesFile = this.getResultFile(dataDispatchEvent.transactionId());
            getFileStream(propertiesFile)
                    .getChannel()
                    .write(ByteBuffer.wrap(String.format("%s(%s)#%s", dataDispatchEvent.dispatchResultType().getCode(),
                                    event.getEventId(), dataDispatchEvent.dataKey())
                            .getBytes(Sprays.UTF_8)));
        } catch (IOException e) {
            throw new SprayDispatchResultHandleException(this, coordinatorMeta, dataKey, e);
        }

    }

    private File getResultFile(String transactionId) {
        String fileName = transactionId;
        String dir = SpraySystemConfigurations.sprayProjectDir();
        if (dir.endsWith("/")) {
            dir = dir.substring(0, dir.length() - 1);
        }
        return new File(dir + "/spray/dispatch/results/" + fileName + ".result");
    }

    private FileOutputStream getFileStream(File file) throws IOException {
        if (! file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        FileOutputStream stream = FILE_STREAM_CACHE.get(file.getAbsolutePath());
        if (stream == null) {
            synchronized (FILE_STREAM_CACHE) {
                if ((stream = FILE_STREAM_CACHE.get(file.getAbsolutePath())) == null) {
                    FILE_STREAM_CACHE.put(file.getAbsolutePath(),
                            stream = new FileOutputStream(file));
                }
            }
        }
        return stream;
    }
}
