package top.spray.executor.basic.common;

import top.spray.core.engine.connection.SprayDataSourceConnection;
import top.spray.core.engine.props.SprayData;
import top.spray.core.intelligence.annotation.SprayAutoAnalyse;
import top.spray.core.intelligence.annotation.SprayVariableSupport;
import top.spray.engine.step.executor.BaseSprayProcessStepExecutor;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SprayAutoAnalyse
public class SprayExecutor_VariableDefiner extends BaseSprayProcessStepExecutor {
    private List<SprayData>
    protected void init0() {
        super.init0();
        this.getMeta()
    }

    /**
     * 数据源连接id
     */
    private String getDatasourceId() {
        return this.datasourceId;
    }

    /**
     * 目标表名
     */
    @SprayVariableSupport
    private String getTableName() {
        return this.tableName;
    }

    private SprayDataSourceConnection<Connection> getConnection() {
        return SprayDataSourceConnection.get(this.getDatasourceId());
    }

    @Override
    protected void execute0(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still, Map<String, Object> processData) {
        Iterator<SprayData> it = read(this.getConnection(), this.getTableName());
        while (it.hasNext()) {
            this.publishData(it.next(), it.hasNext());
        }
    }
}
