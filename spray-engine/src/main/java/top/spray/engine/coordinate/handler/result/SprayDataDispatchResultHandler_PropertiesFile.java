package top.spray.engine.coordinate.handler.result;

import cn.hutool.core.io.FileUtil;
import top.spray.core.config.util.SpraySystemConfigurations;
import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.status.impl.SprayDataDispatchResultStatus;
import top.spray.core.i18n.SprayUtf8s;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class SprayDataDispatchResultHandler_PropertiesFile implements SprayDataDispatchResultHandler {
    public static final SprayDataDispatchResultHandler_PropertiesFile INSTANCE =
            new SprayDataDispatchResultHandler_PropertiesFile();
    private static final String SEP_FOR_COMPUTE = "_DispatchResultHandler_DispatchResultHandler_";
    private static final String SEP_FOR_STATUS = ":=:";
    private static final String nextLineReplace = "\\_n";
    private SprayDataDispatchResultHandler_PropertiesFile() {}
    @Override
    public boolean canDeal(SprayProcessCoordinatorMeta coordinatorMeta) {
        return true;
    }

    @Override
    public String computeDataKey(
            SprayProcessCoordinator coordinator, SprayVariableContainer variables,
            SprayProcessStepExecutor fromExecutor, SprayData data, boolean still,
            SprayProcessStepMeta nextMeta) {
        return (coordinator.getMeta().transactionId() + SEP_FOR_COMPUTE +
                fromExecutor.getExecutorNameKey() + SEP_FOR_COMPUTE +
                nextMeta.getExecutorNameKey(coordinator) + SEP_FOR_COMPUTE +
                still + data.toJson().hashCode()).replace("\n", nextLineReplace);
    }

    @Override
    public void setDispatchResult(SprayProcessCoordinator coordinator, SprayVariableContainer variables,
                                  SprayProcessStepExecutor fromExecutor, SprayData data, boolean still,
                                  SprayProcessStepMeta nextMeta, String dataKey, SprayDataDispatchResultStatus dataDispatchStatus) {
        try {
            File propertiesFile = this.getResultFile(coordinator.getMeta().transactionId());
            if (!propertiesFile.exists()) {
                propertiesFile.getParentFile().mkdirs();
                propertiesFile.createNewFile();
            }
            new FileOutputStream(propertiesFile)
                    .getChannel()
                    .write(ByteBuffer.wrap((dataKey+SEP_FOR_STATUS+dataDispatchStatus.getCode()+"\n")
                            .getBytes(SprayUtf8s.Charset)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SprayDataDispatchResultStatus> getDispatchResult(SprayProcessCoordinator coordinator, String dataKey) {
        try {
            List<SprayDataDispatchResultStatus> resultStatusList = new ArrayList<>();
            String prefix = dataKey+ SEP_FOR_STATUS;
            FileUtil.readLine(new RandomAccessFile(
                    this.getResultFile(coordinator.getMeta().transactionId()), "r"),
                    SprayUtf8s.Charset, l -> {
                        if (l.startsWith(prefix)) {
                            resultStatusList.add(SprayDataDispatchResultStatus
                                    .get(Integer.parseInt(l.substring(prefix.length()).trim())));
                        }
                    });
            return resultStatusList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File getResultFile(String fileName) {
        String dir = SpraySystemConfigurations.sprayProjectDir();
        if (dir.endsWith("/")) {
            dir = dir.substring(0, dir.length() - 1);
        }
        return new File(dir + "/spray/dispatch/results/" + fileName + ".result");
    }
}
