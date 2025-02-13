package top.spray.db.sql.db.mysql;

import top.spray.db.sql.objects.SpraySqlObject;
import top.spray.db.sql.objects.SpraySqlOption;
import top.spray.db.sql.objects.db.SprayDatabaseType;
import top.spray.db.sql.objects.db.SprayMySqlType;

import java.io.IOException;

public interface SpraySqlObjectMysql extends SpraySqlObject {
    // DDD
    @Override
    default SprayDatabaseType databaseType() {
        return SprayMySqlType.INSTANCE;
    }

    default String doEscape(String value) {
        return this.databaseType().doEscape(value);
    }

    default void optionAppend(Appendable appender, SpraySqlOption option) throws IOException {
        if (option == SpraySqlOption.PRETTY) {
            appender.append("\n");
        }
    }

}
