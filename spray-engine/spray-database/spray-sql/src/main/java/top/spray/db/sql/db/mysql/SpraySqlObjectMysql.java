package top.spray.db.sql.db.mysql;

import top.spray.db.sql.db.types.SprayDatabaseType;
import top.spray.db.sql.db.types.SprayMySqlType;
import top.spray.db.sql.objects.SpraySqlObject;
import top.spray.db.sql.objects.SpraySqlOption;

import java.io.IOException;

public interface SpraySqlObjectMysql extends SpraySqlObject {
    // DDD
    @Override
    default SprayDatabaseType databaseType() {
        return SprayMySqlType.INSTANCE;
    }

    default void optionAppend(Appendable appender, SpraySqlOption option) throws IOException {
        if (option == SpraySqlOption.PRETTY) {
            appender.append("\n");
        }
    }

}
