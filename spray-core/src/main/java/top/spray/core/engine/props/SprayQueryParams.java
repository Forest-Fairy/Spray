package top.spray.core.engine.props;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class SprayQueryParams {
    /** the fields to save */
    private Map<String, Long> selectFields;

    /** the target to query */
    private String fromTarget;

    /** values of the fields to cast */
    private Map<String, Class<?>> castmap;

    /** the where conditions */
    private Map<String, Object> where;

    /** a extra sql for query sql */
    private String extraWhereSql;

    /** order by options, key for field and value for direction, n > 0 means asc */
    private Map<String, Long> order;

    /** extra orderBy sql or groupBy sql */
    private String extraOrderByAndGroupBy;

}
