package top.spray.db.sql.condition.value;

public interface SpraySqlNullCondition extends SpraySqlConditionValue<Void> {
    /** like / between / ... */
    String conditionSymbol();

    @Override
    default Void getValue() {
        return null;
    }
}