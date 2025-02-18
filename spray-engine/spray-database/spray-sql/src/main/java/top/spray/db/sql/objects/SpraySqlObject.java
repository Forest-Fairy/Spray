package top.spray.db.sql.objects;

import top.spray.db.sql.db.types.SprayDatabaseType;

import java.io.IOException;

public interface SpraySqlObject {
    /** if object has alias then use its alias else use its name */
    SprayDatabaseType databaseType();

    boolean doEscape();

    String refName();

    /**
     *
     * @param appender
     * @param extraAppender
     * @param option
     * @param action
     * @throws IOException
     */
    void writeSql(Appendable appender, Appendable extraAppender, SpraySqlOption option, SpraySqlAction action) throws IOException;
}
