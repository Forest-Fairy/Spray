package top.spray.db.sql.condition.value;

import top.spray.db.sql.objects.SpraySqlObject;

public interface SpraySqlConditionValue<T> extends SpraySqlObject {
    T getValue();
}
