package top.spray.db.sql.condition.value;

import java.sql.Timestamp;

public interface SpraySqlTimestampCondition extends SpraySqlConditionValue<Timestamp> {
    /** like / between / ... */
    String conditionSymbol();

    @Override
    Timestamp getValue();
}