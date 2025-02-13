package top.spray.db.sql.condition.value;

public interface SpraySqlStringCondition extends SpraySqlConditionValue<String> {
    /** like / between / ... */
    String conditionSymbol();

    @Override
    String getValue();
}
