package top.spray.db.sql.db.oracle;

import top.spray.db.sql.db.types.SprayDatabaseType;
import top.spray.db.sql.db.types.SprayOracleType;
import top.spray.db.sql.objects.SpraySqlObject;
import top.spray.db.sql.objects.SpraySqlOption;

import java.io.IOException;

public interface SpraySqlObjectOracle extends SpraySqlObject {
    // DDD
    @Override
    default SprayDatabaseType databaseType() {
        return SprayOracleType.INSTANCE;
    }

    default void optionAppend(Appendable appender, SpraySqlOption option) throws IOException {
        if (option == SpraySqlOption.PRETTY) {
            appender.append("\n");
        }
    }
}