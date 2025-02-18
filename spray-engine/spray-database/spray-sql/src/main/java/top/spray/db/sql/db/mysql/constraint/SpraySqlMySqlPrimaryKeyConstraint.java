package top.spray.db.sql.db.mysql.constraint;

import top.spray.db.sql.constraint.SpraySqlPrimaryKeyConstraint;
import top.spray.db.sql.db.mysql.SpraySqlObjectMysql;
import top.spray.db.sql.objects.SpraySqlAction;
import top.spray.db.sql.objects.SpraySqlOption;

import java.io.IOException;

public class SpraySqlMySqlPrimaryKeyConstraint implements SpraySqlObjectMysql, SpraySqlPrimaryKeyConstraint {
    private final boolean doOrRemoveEscape;
    private final String name;
    private final String column;

    public SpraySqlMySqlPrimaryKeyConstraint(boolean doOrRemoveEscape, String name, String column) {
        this.doOrRemoveEscape = doOrRemoveEscape;
        this.name = name;
        this.column = column;
    }

    @Override
    public String constraintName() {
        return name;
    }

    @Override
    public String constraintType() {
        return "PRIMARY KEY";
    }

    @Override
    public String onColumn() {
        return column;
    }

    @Override
    public boolean doOrRemoveEscape() {
        return doOrRemoveEscape;
    }

    @Override
    public String refName() {
        return name;
    }

    @Override
    public void writeSql(Appendable appender, Appendable extraAppender, SpraySqlOption option, SpraySqlAction action) throws IOException {
        if (action.isAction(SpraySqlAction.TableAction.CREATE_TABLE)) {
            // CONSTRAINT constraint_name PRIMARY KEY (column_name)
            if (name != null && !name.isEmpty()) {
                extraAppender.append("CONSTRAINT ").append(handleEscape(doOrRemoveEscape, name)).append(" ");
            }
            extraAppender.append("PRIMARY KEY (").append(handleEscape(doOrRemoveEscape, column)).append(")");
        } else if (action.isAction(SpraySqlAction.TableAction.ALTER_TABLE)) {
            if (action.isAction(SpraySqlAction.ConstraintAction.ADD_CONSTRAINT)) {
                // ALTER TABLE table_name ADD CONSTRAINT constraint_name PRIMARY KEY (column_name);
                this.optionAppend(appender, option);
                appender.append("ADD ");
                if (name != null && !name.isEmpty()) {
                    appender.append("CONSTRAINT ").append(handleEscape(doOrRemoveEscape, name)).append(" ");
                }
                appender.append("PRIMARY KEY (").append(handleEscape(doOrRemoveEscape, column)).append(")");
            } else if (action.isAction(SpraySqlAction.ConstraintAction.DROP_CONSTRAINT)) {
                // ALTER TABLE table_name DROP PRIMARY KEY;
                this.optionAppend(appender, option);
                appender.append("DROP PRIMARY KEY");
            }
        }
    }
} 