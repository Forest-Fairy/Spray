package top.spray.db.connection.example;

import top.spray.db.connection.connection.jdbc.SprayJDBCConnection;
import top.spray.db.connection.support.SprayTransactionSupportConnection;
import top.spray.db.sql.column.SpraySqlColumnDefinition;
import top.spray.db.sql.db.oracle.SpraySqlObjectOracle;
import top.spray.db.sql.objects.SpraySqlOption;
import top.spray.db.sql.objects.db.SprayOracleType;
import top.spray.db.sql.table.create.SpraySqlCreateTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class SpraySqlExample {

    public static void main(String[] args) {
        try (SprayJDBCConnection connection = createConnection()) {
            SpraySqlCreateTable createTable = createTable();
            StringBuilder sqlBuilder = new StringBuilder();
            StringBuilder extraBuilder = new StringBuilder();
            try {
                createTable.writeSql(sqlBuilder, extraBuilder, SpraySqlOption.PRETTY, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String sql = sqlBuilder.toString() + extraBuilder.toString();
            System.out.println("Generated SQL: " + sql);
            connection.getPreparedStatement(sql, null).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static SprayJDBCConnection createConnection() throws SQLException {
        Connection jdbcConnection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "username", "password");
        return new SprayJDBCConnection(jdbcConnection);
    }

    private static SpraySqlCreateTable createTable() {
        SpraySqlColumnDefinition column1 = new SpraySqlOracleColumnDefinition("id", null, "NUMBER", "Primary Key")
                .addConstraint(new SpraySqlOraclePrimaryKeyConstraint("pk_id", "id"))
                .addConstraint(new SpraySqlOracleNotNullConstraint("nn_id", "id", "NUMBER"));

        SpraySqlColumnDefinition column2 = new SpraySqlOracleColumnDefinition("name", null, "VARCHAR2(50)", "Name")
                .addConstraint(new SpraySqlOracleNotNullConstraint("nn_name", "name", "VARCHAR2(50)"));

        List<SpraySqlColumnDefinition> columns = List.of(column1, column2);

        return new SpraySqlCreateTableImpl(new SpraySqlNamedTableImpl("employees"), columns, List.of());
    }

    private static class SpraySqlNamedTableImpl implements SpraySqlNamedTable, SpraySqlObjectOracle {
        private final String tableName;

        public SpraySqlNamedTableImpl(String tableName) {
            this.tableName = tableName;
        }

        @Override
        public String tableName() {
            return tableName;
        }

        @Override
        public String alias() {
            return tableName;
        }

        @Override
        public String refName() {
            return tableName;
        }

        @Override
        public void writeSql(Appendable appender, Appendable extraAppender, SpraySqlOption option, SpraySqlAction action) throws IOException {
            appender.append(doEscape(tableName));
        }
    }
}
