package top.spray.db.sql.db.mysql.constraint;

import top.spray.db.sql.constraint.SpraySqlForeignKeyConstraint;
import top.spray.db.sql.db.mysql.SpraySqlObjectMysql;
import top.spray.db.sql.objects.SpraySqlAction;
import top.spray.db.sql.objects.SpraySqlOption;

import java.io.IOException;

public class SpraySqlMySqlForeignKeyConstraint implements SpraySqlObjectMysql, SpraySqlForeignKeyConstraint {
    private final String name;
    private final String column;
    private final String referenceTable;
    private final String referenceColumn;

    public SpraySqlMySqlForeignKeyConstraint(String name, String column, String referenceTable, String referenceColumn) {
        this.name = name;
        this.column = column;
        this.referenceTable = referenceTable;
        this.referenceColumn = referenceColumn;
    }

    @Override
    public String constraintName() {
        return name;
    }

    @Override
    public String constraintType() {
        return "FOREIGN KEY";
    }
    
    @Override
    public String onColumn() {
        return column;
    }

    @Override
    public String referenceTable() {
        return referenceTable;
    }

    @Override
    public String referenceColumn() {
        return referenceColumn;
    }

    @Override
    public String refName() {
        return name;
    }

    @Override
    public void writeSql(Appendable appender, Appendable extraAppender, SpraySqlOption option, SpraySqlAction action) throws IOException {
        if (action.isAction(SpraySqlAction.TableAction.CREATE_TABLE)) {
            // FOREIGN KEY (vend_id) REFERENCES Vendors (vend_id)
            this.optionAppend(appender, option);
            if (name != null && !name.isEmpty()) {
                extraAppender.append("CONSTRAINT ").append(doEscape(name)).append(" ");
            }
            extraAppender.append("FOREIGN KEY (").append(doEscape(column)).append(") ")
                .append("REFERENCES ").append(doEscape(referenceTable))
                .append(" (").append(doEscape(referenceColumn)).append(")");
        } else if (action.isAction(SpraySqlAction.TableAction.ALTER_TABLE)) {
            // ALTER TABLE t_info ADD FOREIGN KEY (user_id) REFERENCES t_user(id);
            if (action.isAction(SpraySqlAction.ConstraintAction.ADD_CONSTRAINT)) {
                this.optionAppend(appender, option);
                appender.append("ADD ");
                if (name != null && !name.isEmpty()) {
                    appender.append("CONSTRAINT ").append(doEscape(name)).append(" ");
                }
                appender.append("FOREIGN KEY (").append(doEscape(column)).append(") ")
                   .append("REFERENCES ").append(doEscape(referenceTable))
                   .append(" (").append(doEscape(referenceColumn)).append(")");
            } else if (action.isAction(SpraySqlAction.ConstraintAction.DROP_CONSTRAINT)) {
                this.optionAppend(appender, option);
                appender.append("DROP FOREIGN KEY ").append(doEscape(name)).append(" ");
            }
        }
    }
} 