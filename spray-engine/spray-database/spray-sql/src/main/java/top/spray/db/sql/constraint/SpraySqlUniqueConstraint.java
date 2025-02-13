package top.spray.db.sql.constraint;

public interface SpraySqlUniqueConstraint extends SpraySqlConstraint {
    String onColumn();
}
