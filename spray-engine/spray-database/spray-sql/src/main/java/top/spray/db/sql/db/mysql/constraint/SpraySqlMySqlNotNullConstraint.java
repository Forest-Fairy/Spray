package top.spray.db.sql.db.mysql.constraint;

import top.spray.db.sql.constraint.SpraySqlNotNullConstraint;
import top.spray.db.sql.db.mysql.SpraySqlObjectMysql;
import top.spray.db.sql.objects.SpraySqlAction;
import top.spray.db.sql.objects.SpraySqlOption;

import java.io.IOException;

public class SpraySqlMySqlNotNullConstraint implements SpraySqlObjectMysql, SpraySqlNotNullConstraint {
    private final String name;
    private final String column;
    private final String typeToken;
    public SpraySqlMySqlNotNullConstraint(String name, String column, String typeToken) {
        this.name = name;
        this.column = column;
        this.typeToken = typeToken;
    }

    @Override
    public String constraintName() {
        return name;
    }

    @Override
    public String onColumn() {
        return column;
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
    public String constraintType() {
        return "NOT NULL";
    }

    @Override
    public void writeSql(Appendable appender, Appendable extraAppender, SpraySqlOption option, SpraySqlAction action) throws IOException {
        if (action.isAction(SpraySqlAction.TableAction.CREATE_TABLE)) {
            // NOT NULL
            appender.append(" NOT NULL");
        } else if (action.isAction(SpraySqlAction.TableAction.ALTER_TABLE)) {
            if (action.isAction(SpraySqlAction.ConstraintAction.ADD_CONSTRAINT)) {
                // ALTER TABLE table_name MODIFY column_name datatype NOT NULL;
                this.optionAppend(appender, option);
                appender.append("MODIFY ").append(doEscape(column)).append(typeToken).append(" NOT NULL");
            } else if (action.isAction(SpraySqlAction.ConstraintAction.DROP_CONSTRAINT)) {
                // ALTER TABLE table_name MODIFY column_name datatype;
                this.optionAppend(appender, option);
                appender.append("MODIFY ").append(doEscape(column)).append(typeToken);
            }
        }
    }
} 