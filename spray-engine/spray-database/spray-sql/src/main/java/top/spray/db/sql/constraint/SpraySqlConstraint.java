package top.spray.db.sql.constraint;

import top.spray.db.sql.objects.SpraySqlObject;

public interface SpraySqlConstraint extends SpraySqlObject {
    String constraintName();
    String constraintType();
}
