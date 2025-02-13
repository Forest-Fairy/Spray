package top.spray.db.sql.db.mysql.constraint;

import top.spray.db.sql.constraint.SpraySqlDefaultValueConstraint;
import top.spray.db.sql.db.mysql.SpraySqlObjectMysql;
import top.spray.db.sql.objects.SpraySqlAction;
import top.spray.db.sql.objects.SpraySqlOption;

import java.io.IOException;

public class SpraySqlMySqlDefaultValueConstraint implements SpraySqlObjectMysql, SpraySqlDefaultValueConstraint {
    private final String name;
    private final String column;
    private final String defaultValue;
    private final String typeToken;

    public SpraySqlMySqlDefaultValueConstraint(String name, String column, String defaultValue, String typeToken) {
        this.name = name;
        this.column = column;
        this.defaultValue = defaultValue;
        this.typeToken = typeToken;
    }

    @Override
    public String constraintName() {
        return name;
    }

    @Override
    public String constraintType() {
        return "DEFAULT";
    }

    @Override
    public String onColumn() {
        return column;
    }

    @Override
    public String defaultValue() {
        return defaultValue;
    }

    @Override
    public String typeToken() {
        return typeToken;
    }

    @Override
    public String refName() {
        return name;
    }

    @Override
    public void writeSql(Appendable appender, Appendable extraAppender, SpraySqlOption option, SpraySqlAction action) throws IOException {
        if (action.isAction(SpraySqlAction.TableAction.CREATE_TABLE)) {
            appender.append(" DEFAULT ").append(databaseType().formatValue(defaultValue, typeToken));
        } else if (action.isAction(SpraySqlAction.TableAction.ALTER_TABLE)) {
            if (action.isAction(SpraySqlAction.ConstraintAction.ADD_CONSTRAINT)) {
                // ALTER TABLE table_name ALTER column_name SET DEFAULT xxx;
                this.optionAppend(appender, option);
                appender.append("ALTER ").append(doEscape(column));
                appender.append(" SET DEFAULT ").append(databaseType().formatValue(defaultValue, typeToken));
            } else if (action.isAction(SpraySqlAction.ConstraintAction.DROP_CONSTRAINT)) {
                // ALTER TABLE table_name ALTER column_name DROP DEFAULT;
                this.optionAppend(appender, option);
                appender.append("ALTER ").append(doEscape(column));
                appender.append(" DROP DEFAULT");
            }
        }
    }

} 