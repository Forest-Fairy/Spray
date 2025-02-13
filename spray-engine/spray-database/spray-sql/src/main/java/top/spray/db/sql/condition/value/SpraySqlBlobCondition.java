package top.spray.db.sql.condition.value;

public interface SpraySqlBlobCondition extends SpraySqlConditionValue<byte[]> {
    /** like / between / ... */
    String conditionSymbol();

    @Override
    byte[] getValue();
}