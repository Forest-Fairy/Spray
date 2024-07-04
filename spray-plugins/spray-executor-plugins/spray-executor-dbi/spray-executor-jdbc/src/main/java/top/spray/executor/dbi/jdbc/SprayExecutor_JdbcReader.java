package top.spray.executor.dbi.jdbc;

import top.spray.core.engine.connection.SprayDataSourceConnection;
import top.spray.core.engine.props.SprayData;
import top.spray.core.intelligence.annotation.SprayAutoAnalyse;
import top.spray.core.intelligence.annotation.SprayVariableSupport;
import top.spray.engine.step.executor.SprayBaseStepExecutor;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

import java.sql.Connection;
import java.util.Iterator;
import java.util.Map;

@SprayAutoAnalyse
public class SprayExecutor_JdbcReader extends SprayBaseStepExecutor {
    private SprayDataSourceConnection<Connection> connection;

    /**
     * 数据源连接id
     */
    private String datasourceId;
    /**
     * 目标表名
     */
    @SprayVariableSupport
    private String tableName;

    @Override
    protected void initOnlyAtCreate0() {
        super.initOnlyAtCreate0();
        this.datasourceId = this.getMeta().getString("datasourceId");
        this.tableName = this.getMeta().getString("tableName");
    }

    private String getDatasourceId() {
        return this.datasourceId;
    }

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
