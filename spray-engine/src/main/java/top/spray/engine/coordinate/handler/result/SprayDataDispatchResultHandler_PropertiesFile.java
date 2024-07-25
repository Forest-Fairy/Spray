package top.spray.engine.coordinate.handler.result;

import cn.hutool.core.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;
import top.spray.core.config.util.SpraySystemConfigurations;
import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.types.data.dispatch.result.SprayDataDispatchResultStatus;
import top.spray.core.util.SprayUtf8s;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.exception.SprayDispatchResultHandleException;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SprayDataDispatchResultHandler_PropertiesFile implements SprayDataDispatchResultHandler {
    public static final SprayDataDispatchResultHandler_PropertiesFile INSTANCE =
            new SprayDataDispatchResultHandler_PropertiesFile();
    private static final String SEP_FOR_COMPUTE = "_DispatchResultHandler_DispatchResultHandler_";
    private static final String SEP_FOR_STATUS = ":=:";
    private static final String nextLineReplace = "\\_n";

    private static final Map<String, FileOutputStream> FILE_STREAM_CACHE = new ConcurrentHashMap<>();

    private SprayDataDispatchResultHandler_PropertiesFile() {}
    @Override
    public boolean canDeal(SprayProcessCoordinatorMeta coordinatorMeta) {
        return true;
    }

    @Override
    public String computeDataKey(
            SprayProcessCoordinatorMeta coordinatorMeta, SprayVariableContainer variables,
            SprayProcessStepExecutor fromExecutor, SprayData data, boolean still,
            SprayProcessStepMeta nextMeta) {
        return (coordinatorMeta.transactionId() + SEP_FOR_COMPUTE +
                fromExecutor.getExecutorNameKey() + SEP_FOR_COMPUTE +
                nextMeta.getExecutorNameKey(coordinatorMeta) + SEP_FOR_COMPUTE +
                still + data.toJson().hashCode()).replace("\n", nextLineReplace);
    }

    @Override
    public void setDispatchResult(SprayProcessCoordinatorMeta coordinatorMeta, SprayVariableContainer variables,
                                  SprayProcessStepExecutor fromExecutor, SprayData data, boolean still,
                                  SprayProcessStepMeta nextMeta, String dataKey, SprayDataDispatchResultStatus dataDispatchStatus) {
        try {
            File propertiesFile = this.getResultFile(coordinatorMeta);
            getFileStream(propertiesFile)
                    .getChannel()
                    .write(ByteBuffer.wrap((dataKey+SEP_FOR_STATUS+dataDispatchStatus.getCode()+"\n")
                            .getBytes(SprayUtf8s.Charset)));
        } catch (IOException e) {
            throw new SprayDispatchResultHandleException(this, coordinatorMeta, dataKey, e);
        }
    }

    @Override
    public List<SprayDataDispatchResultStatus> getDispatchResult(SprayProcessCoordinatorMeta coordinatorMeta, String dataKey) {
        try {
            List<SprayDataDispatchResultStatus> resultStatusList = new ArrayList<>();
            String prefix = dataKey + SEP_FOR_STATUS;
            File resultFile = this.getResultFile(coordinatorMeta);
            if (resultFile.exists()) {
                try (RandomAccessFile file = new RandomAccessFile(resultFile, "r")) {
                    String line;
                    while (StringUtils.isNotBlank(line = file.readLine())) {
                        line = CharsetUtil.convert(line, CharsetUtil.CHARSET_ISO_8859_1, SprayUtf8s.Charset);
                        if (line.startsWith(prefix)) {
                            resultStatusList.add(SprayDataDispatchResultStatus
                                    .get(Integer.parseInt(line.substring(prefix.length()).trim())));
                        }
                    }
                }
            }
            return resultStatusList;
        } catch (IOException e) {
            throw new SprayDispatchResultHandleException(this, coordinatorMeta, dataKey, e);
        }
    }

    @Override
    public void whenCoordinatorShutdown(SprayProcessCoordinatorMeta coordinatorMeta) {
        File propertiesFile = getResultFile(coordinatorMeta);
        try {
            getFileStream(propertiesFile).close();
        } catch (Exception ignored) {}
    }

    private File getResultFile(SprayProcessCoordinatorMeta coordinatorMeta) {
        String fileName = coordinatorMeta.transactionId();
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
