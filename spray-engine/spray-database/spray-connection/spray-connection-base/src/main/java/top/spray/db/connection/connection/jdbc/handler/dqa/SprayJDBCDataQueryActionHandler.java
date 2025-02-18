package top.spray.db.connection.connection.jdbc.handler.dqa;

import top.spray.common.data.SprayData;
import top.spray.core.stream.SprayDataProvider;
import top.spray.core.stream.SprayDataStream;
import top.spray.common.tools.SprayOptional;
import top.spray.db.connection.action.SprayAutoRegisterActionHandler;
import top.spray.db.connection.action.dqa.SprayDataQueryAction;
import top.spray.db.connection.action.dqa.SprayDataQueryResult;
import top.spray.db.connection.connection.jdbc.SprayJDBCConnection;
import top.spray.db.connection.exception.SprayDatabaseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class SprayJDBCDataQueryActionHandler extends SprayAutoRegisterActionHandler<SprayJDBCConnection, SprayDataQueryAction, SprayDataQueryResult> {
    protected abstract PreparedStatement getPreparedStatement(String parameterizedSql, SprayJDBCConnection connection, SprayDataQueryAction action) throws Exception;
    @Override
    protected SprayDataQueryResult doHandle(String parameterizedSql, SprayJDBCConnection connection, SprayDataQueryAction action) throws Exception {
        PreparedStatement preparedStatement = getPreparedStatement(parameterizedSql, connection, action);
        ResultSet resultSet = preparedStatement.executeQuery();
        Map<String, Function<Object, Object>> castors = action.castors();
        List<Function<Object, Object>> sortedCastors = new ArrayList<>(action.selectFields().size());
        SprayDataProvider provider = SprayDataStream.newDataProvider();
        SprayDataStream dataStream = new SprayDataStream(provider, null);
        String curColName = null;
        try {
            while (resultSet.next()) {
                SprayData sprayData = new SprayData();
                ResultSetMetaData rsMeta = resultSet.getMetaData();
                for (int i = 0; i < rsMeta.getColumnCount(); i++) {
                    curColName = rsMeta.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    if (sortedCastors.size() < i + 1) {
                        sortedCastors.add(castors.get(curColName));
                    }
                    sprayData.put(curColName,
                            SprayOptional.of(sortedCastors.get(i))
                                    .map(castor -> castor.apply(value))
                                    .orElse(value)
                    );
                }
                dataStream.push(sprayData);

            }
        } catch (Exception e) {
            if (curColName == null) {
                throw new SprayDatabaseException(e, "jdbc.resultSet.error");
            }
            throw new SprayDatabaseException(e, "jdbc.resultSet.column.cast.error", curColName);
        } finally {
            try {
                resultSet.close();
            } catch (Exception ignored) {
            }
            try {
                preparedStatement.close();
            } catch (Exception ignored) {
            }
        }
        return new SprayDataQueryResult() {
            @Override
            public SprayDataStream getDataStream() {
                return dataStream;
            }

            @Override
            public SprayDataQueryAction getAction() {
                return action;
            }
        };
    }
}
