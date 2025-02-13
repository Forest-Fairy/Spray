package top.spray.db.sql.condition.value;

public interface SpraySqlBooleanCondition extends SpraySqlConditionValue<Boolean> {
    /** like / between / ... */
    String conditionSymbol();

    @Override
    Boolean getValue();
}