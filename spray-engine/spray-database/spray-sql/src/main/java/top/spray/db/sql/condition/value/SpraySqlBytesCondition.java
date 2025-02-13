package top.spray.db.sql.condition.value;

public interface SpraySqlBytesCondition extends SpraySqlConditionValue<byte[]> {
    /** like / between / ... */
    String conditionSymbol();

    @Override
    byte[] getValue();

}
