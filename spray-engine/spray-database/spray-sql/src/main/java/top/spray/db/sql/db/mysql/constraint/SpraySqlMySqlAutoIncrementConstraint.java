package top.spray.db.sql.db.mysql.constraint;

import top.spray.db.sql.constraint.SpraySqlNotNullConstraint;
import top.spray.db.sql.db.mysql.SpraySqlObjectMysql;
import top.spray.db.sql.objects.SpraySqlAction;
import top.spray.db.sql.objects.SpraySqlOption;

import java.io.IOException;

public class SpraySqlMySqlAutoIncrementConstraint implements SpraySqlObjectMysql, SpraySqlNotNullConstraint {
    private final boolean doOrRemoveEscape;
    private final String name;
    private final String column;
    private final String typeToken;
    public SpraySqlMySqlAutoIncrementConstraint(boolean doOrRemoveEscape, String name, String column, String typeToken) {
        this.doOrRemoveEscape = doOrRemoveEscape;
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
    public boolean doOrRemoveEscape() {
        return doOrRemoveEscape;
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

        if (action.isAction(SpraySqlAction.TableAction.CREATE_TABLE)) {        // CREATE TABLE tb_student (
            //  id INT PRIMARY KEY AUTO_INCREMENT,
            //  name VARCHAR(25) NOT NULL
            // );
            appender.append(" AUTO_INCREMENT");
        } else if (action.isAction(SpraySqlAction.TableAction.ALTER_TABLE)) {
            if (action.isAction(SpraySqlAction.ConstraintAction.ADD_CONSTRAINT)) {
                // ALTER TABLE tablename CHANGE uid INT PRIMARY KEY AUTO_INCREMENT;
                this.optionAppend(appender, option);
                appender.append("CHANGE ");
                appender.append(handleEscape(doOrRemoveEscape, column)).append(" ").append(typeToken).append(" AUTO_INCREMENT");
            } else if (action.isAction(SpraySqlAction.ConstraintAction.DROP_CONSTRAINT)) {
                // ALTER TABLE `members` CHANGE uid INT;
                this.optionAppend(appender, option);
                appender.append("CHANGE ");
                appender.append(handleEscape(doOrRemoveEscape, column)).append(" ").append(typeToken);
            }
        }
    }
} 