package top.spray.db.sql.objects;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.time.*;
import java.util.HashMap;
import java.util.Map;

public class SprayJdbcTypeMapping {

    private static final BiMap<Class<?>, Integer> SQL_TYPE_MAP;

    public static int toJdbcType(Class<?> type) {
        Integer jdbcType = SQL_TYPE_MAP.get(type);
        if (jdbcType == null) {
            // use long varchar by default
            jdbcType = Types.LONGVARCHAR;
        }
        return jdbcType;
    }
    public static Class<?> toJavaType(int sqlType) {
        Class<?> javaType = SQL_TYPE_MAP.inverse().get(sqlType);
        if (javaType == null) {
            // use string by default
            javaType = String.class;
        }
        return javaType;
    }

    static {
        Map<Class<?>, Integer> map = new HashMap<>();
        map.put(String.class, Types.VARCHAR);
        map.put(boolean.class, Types.BOOLEAN);
        map.put(Boolean.class, Types.BOOLEAN);
        map.put(char.class, Types.CHAR);
        map.put(Character.class, Types.CHAR);
        map.put(byte.class, Types.TINYINT);
        map.put(Byte.class, Types.TINYINT);
        map.put(short.class, Types.SMALLINT);
        map.put(Short.class, Types.SMALLINT);
        map.put(int.class, Types.INTEGER);
        map.put(Integer.class, Types.INTEGER);
        map.put(long.class, Types.BIGINT);
        map.put(Long.class, Types.BIGINT);
        map.put(float.class, Types.FLOAT);
        map.put(Float.class, Types.FLOAT);
        map.put(double.class, Types.DOUBLE);
        map.put(Double.class, Types.DOUBLE);
        map.put(BigInteger.class, Types.DECIMAL);
        map.put(BigDecimal.class, Types.DECIMAL);
        map.put(java.sql.Date.class, Types.DATE);
        map.put(java.sql.Time.class, Types.TIME);
        map.put(java.util.Date.class, Types.TIMESTAMP);
        map.put(java.sql.Timestamp.class, Types.TIMESTAMP);
        map.put(LocalDate.class, Types.DATE);
        map.put(LocalTime.class, Types.TIME);
        map.put(LocalDateTime.class, Types.TIMESTAMP);
        map.put(OffsetDateTime.class, Types.TIMESTAMP_WITH_TIMEZONE);
        map.put(ZonedDateTime.class, Types.TIMESTAMP_WITH_TIMEZONE);
        map.put(byte[].class, Types.BINARY);
        SQL_TYPE_MAP = HashBiMap.create(map);
    }
}
