package top.spray.db.sql.constraint;

public interface SpraySqlPrimaryKeyConstraint extends SpraySqlConstraint {
    String onColumn();
}
