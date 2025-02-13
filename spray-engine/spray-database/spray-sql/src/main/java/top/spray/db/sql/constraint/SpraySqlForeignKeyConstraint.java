package top.spray.db.sql.constraint;

public interface SpraySqlForeignKeyConstraint extends SpraySqlConstraint {
    String onColumn();
    String referenceTable();
    String referenceColumn();
}