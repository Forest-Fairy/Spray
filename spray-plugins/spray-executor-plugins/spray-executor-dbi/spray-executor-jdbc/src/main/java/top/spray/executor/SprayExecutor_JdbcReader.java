package top.spray.executor;

import top.spray.core.engine.connection.SprayDataSourceConnection;
import top.spray.core.engine.props.SprayData;
import top.spray.core.intelligence.annotation.SprayAutoAnalyse;
import top.spray.core.intelligence.annotation.SprayVariableSupport;
import top.spray.engine.step.executor.BaseSprayProcessStepExecutor;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

import java.sql.Connection;
import java.util.IdentityHashMap;
import java.util.Iterator;

@SprayAutoAnalyse
public class SprayExecutor_JdbcReader extends BaseSprayProcessStepExecutor {
    private SprayDataSourceConnection<Connection> connection;
    private String datasourceId;
    private String tableName;

    @Override
    protected void init0() {
        super.init0();
        this.datasourceId = this.getMeta().getString("datasourceId");
        this.tableName = this.getMeta().getString("tableName");
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
    protected void execute0(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        Iterator<SprayData> it = read(this.getConnection(), this.getTableName());
        while (it.hasNext()) {
            this.publishData(it.next(), it.hasNext());
        }
    }
}
