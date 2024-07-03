package top.spray.executor.basic.compute;

import top.spray.core.engine.connection.SprayDataSourceConnection;
import top.spray.core.engine.props.SprayData;
import top.spray.core.intelligence.annotation.SprayAutoAnalyse;
import top.spray.core.intelligence.annotation.SprayVariableSupport;
import top.spray.engine.step.executor.BaseSprayProcessStepExecutor;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

import java.sql.Connection;
import java.util.Iterator;
import java.util.Map;


/**
 * data filter
 */
@SprayAutoAnalyse(description = "data filter")
public class SprayExecutor_DataFilter extends BaseSprayProcessStepExecutor {

    @Override
    protected void init0() {
        super.init0();
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
