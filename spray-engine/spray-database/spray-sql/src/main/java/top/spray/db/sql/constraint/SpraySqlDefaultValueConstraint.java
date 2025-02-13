package top.spray.db.sql.constraint;

public interface SpraySqlDefaultValueConstraint extends SpraySqlConstraint {
    String onColumn();
    String typeToken();
    String defaultValue();
}
