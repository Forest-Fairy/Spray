package top.spray.db.sql.db.mysql.constraint;

import top.spray.db.sql.constraint.SpraySqlUniqueConstraint;
import top.spray.db.sql.db.mysql.SpraySqlObjectMysql;
import top.spray.db.sql.objects.SpraySqlAction;
import top.spray.db.sql.objects.SpraySqlOption;
import top.spray.db.sql.db.types.SprayDatabaseType;
import top.spray.db.sql.db.types.SprayMySqlType;

import java.io.IOException;

public class SpraySqlMySqlUniqueConstraint implements SpraySqlObjectMysql, SpraySqlUniqueConstraint {
    private final boolean doEscape;
    private final String constraintName;
    private final String column;

    public SpraySqlMySqlUniqueConstraint(boolean doEscape, String constraintName, String column) {
        this.doEscape = doEscape;
        this.constraintName = constraintName;
        this.column = column;
    }

    @Override
    public String constraintName() {
        return constraintName;
    }

    @Override
    public String constraintType() {
        return "UNIQUE";
    }
    
    @Override
    public String onColumn() {
        return column;
    }
    
    @Override
    public SprayDatabaseType databaseType() {
        return SprayMySqlType.INSTANCE;
    }

    @Override
    public boolean doEscape() {
        return doEscape;
    }

    @Override
    public String refName() {
        return constraintName;
    }

    @Override
    public void writeSql(Appendable appender, Appendable extraAppender, SpraySqlOption option, SpraySqlAction action) throws IOException {
        if (action.isAction(SpraySqlAction.TableAction.CREATE_TABLE)) {
            // CONSTRAINT constraint_name UNIQUE (column_name)
            if (constraintName != null && !constraintName.isEmpty()) {
                extraAppender.append("CONSTRAINT ").append(doEscape(constraintName)).append(" ");
            }
            extraAppender.append("UNIQUE (").append(doEscape(column)).append(")");
        } else if (action.isAction(SpraySqlAction.TableAction.ALTER_TABLE)) {
            if (action.isAction(SpraySqlAction.ConstraintAction.ADD_CONSTRAINT)) {
                // ALTER TABLE table_name ADD CONSTRAINT constraint_name UNIQUE (column_name);
                this.optionAppend(appender, option);
                appender.append("ADD ");
                if (constraintName != null && !constraintName.isEmpty()) {
                    appender.append("CONSTRAINT ").append(doEscape(constraintName)).append(" ");
                }
                appender.append("UNIQUE (").append(doEscape(column)).append(")");
            } else if (action.isAction(SpraySqlAction.ConstraintAction.DROP_CONSTRAINT)) {
                // ALTER TABLE table_name DROP INDEX constraint_name;
                this.optionAppend(appender, option);
                appender.append("DROP INDEX ").append(doEscape(constraintName));
            }
        }
    }
} 