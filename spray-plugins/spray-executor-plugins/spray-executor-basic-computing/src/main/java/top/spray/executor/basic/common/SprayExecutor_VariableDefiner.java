package top.spray.executor.basic.common;

import top.spray.core.engine.connection.SprayDataSourceConnection;
import top.spray.core.engine.props.SprayData;
import top.spray.core.intelligence.annotation.SprayClassInfoAutoAnalyse;
import top.spray.core.intelligence.annotation.SprayVariableSupport;
import top.spray.engine.step.executor.SprayBaseStepExecutor;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SprayClassInfoAutoAnalyse
public class SprayExecutor_VariableDefiner extends SprayBaseStepExecutor {
    private List<SprayData>
    protected void initOnlyAtCreate0() {
        super.initOnlyAtCreate0();
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
