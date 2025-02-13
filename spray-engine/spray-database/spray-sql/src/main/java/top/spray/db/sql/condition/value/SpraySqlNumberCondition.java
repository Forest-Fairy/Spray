package top.spray.db.sql.condition.value;

public interface SpraySqlNumberCondition extends SpraySqlConditionValue<Number> {
    /** like / between / ... */
    String conditionSymbol();

    @Override
    Number getValue();
}