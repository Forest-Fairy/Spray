package top.spray.db.sql.constraint;

public interface SpraySqlNotNullConstraint extends SpraySqlConstraint {
    String onColumn();
    String typeToken();
} 