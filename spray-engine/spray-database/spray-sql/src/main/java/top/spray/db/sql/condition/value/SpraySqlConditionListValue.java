package top.spray.db.sql.condition.value;

import java.util.List;

/**
 * the definition of a series of conditions
 *  such as: A and (1 = 1 and b and c and d) means 'and b and c and d'
 *
 */
public interface SpraySqlConditionListValue extends SpraySqlConditionValue<List<SpraySqlConditionValue<?>>> {
    @Override
    List<SpraySqlConditionValue<?>> getValue();
}
