package top.spray.db.sql.condition;

import top.spray.db.sql.condition.value.SpraySqlConditionValue;
import top.spray.db.sql.objects.SpraySqlObject;

public interface SpraySqlCondition extends SpraySqlObject {
    boolean withAndOr();
    boolean negative();
    SpraySqlConditionValue<?> condition();
}
