package top.spray.db.sql.db.mysql.column;

import top.spray.db.sql.column.SpraySqlColumnDefinition;
import top.spray.db.sql.constraint.SpraySqlConstraint;
import top.spray.db.sql.db.mysql.SpraySqlObjectMysql;
import top.spray.db.sql.objects.SpraySqlAction;
import top.spray.db.sql.objects.SpraySqlOption;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SpraySqlMySqlColumnDefinition implements SpraySqlObjectMysql, SpraySqlColumnDefinition {
    private final String colName;
    private final String typeToken;
    private final String comment;
    private final Map<String, SpraySqlConstraint> constraints;
    private final String lastColName;

    public SpraySqlMySqlColumnDefinition(String colName, String lastColName, String typeToken, String comment) {
        this.colName = colName;
        this.lastColName = lastColName;
        this.typeToken = typeToken;
        this.comment = comment;
        this.constraints = new HashMap<>(6);
    }

    @Override
    public String colName() {
        return colName;
    }

    @Override
    public String typeToken() {
        return typeToken;
    }

    @Override
    public String lastColName() {
        return lastColName;
    }

    @Override
    public Map<String, SpraySqlConstraint> constraints() {
        return constraints;
    }

    public SpraySqlMySqlColumnDefinition addConstraint(SpraySqlConstraint constraint) {
        constraints.put(constraint.constraintType(), constraint);
        return this;
    }

    @Override
    public String comment() {
        return comment;
    }

    @Override
    public String refName() {
        return colName();
    }

    @Override
    public void writeSql(Appendable appender, Appendable extraAppender, SpraySqlOption option, SpraySqlAction action) throws IOException {
         if (action.isAction(SpraySqlAction.TableAction.CREATE_TABLE)) {
            this.optionAppend(appender, option);
            appender.append(" ").append(doOrRemoveEscape(colName)).append(" ")
                .append(typeToken).append(" ");

            List<String> extraLines = new LinkedList<>();
            StringBuilder extra = new StringBuilder();
            for (SpraySqlConstraint constraint : constraints.values()) {    
                constraint.writeSql(appender, extra, option, action);
                if (!extra.isEmpty()) {
                    extraLines.add(extra.toString());
                    extra.delete(0, extra.length());
                }
            }

            if (comment != null && !comment.isEmpty()) {
                appender.append(" COMMENT '").append(comment).append("'");
            }
            appender.append(",");
            // some extra lines
            for (String extraLine : extraLines) {
                this.optionAppend(extraAppender, option);
                extraAppender.append(extraLine).append(",");
            }
        } else if (action.isAction(SpraySqlAction.ColumnAction.ALTER_COLUMN_CONSTRAINT)) {
            // ALTER TABLE: ADD CONSTRAINT
             this.optionAppend(appender, option);
             for (SpraySqlConstraint constraint : constraints.values()) {
                 constraint.writeSql(appender, new StringBuilder(), option, action);
             }

        } else if (action.isAction(SpraySqlAction.ColumnAction.ADD_COLUMN)) {
             // ALTER TABLE tableName ADD colName colType (AFTER colName0);
             this.optionAppend(appender, option);
             appender.append("ADD ").append(doOrRemoveEscape(colName)).append(" ")
                 .append(typeToken).append(" ");
             if (lastColName != null && !lastColName.isEmpty()) {
                 appender.append("AFTER ").append(doOrRemoveEscape(lastColName));
             }
        } else if (action.isAction(SpraySqlAction.ColumnAction.DROP_COLUMN)) {
             // ALTER TABLE tableName DROP colName;
             this.optionAppend(appender, option);
             appender.append("DROP ").append(doOrRemoveEscape(colName));
        } else if (action.isAction(SpraySqlAction.ColumnAction.ALTER_COLUMN_STRUCT)) {
             // ALTER TABLE tableName CHANGE colName newColName newColType;
             // ALTER TABLE tableName MODIFY colName colType;
             if (lastColName != null && !lastColName.isEmpty()) {
                 appender.append("CHANGE ").append(doOrRemoveEscape(lastColName)).append(" ")
                         .append(doOrRemoveEscape(colName)).append(" ").append(typeToken);
             } else {
                 appender.append("MODIFY ").append(doOrRemoveEscape(colName)).append(" ").append(typeToken);
             }
        }
    }
} 