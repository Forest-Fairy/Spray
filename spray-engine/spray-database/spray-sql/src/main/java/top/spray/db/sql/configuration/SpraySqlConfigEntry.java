package top.spray.db.sql.configuration;

import top.spray.common.tools.SprayTuple;
import top.spray.db.sql.objects.SpraySqlObject;

public interface SpraySqlConfigEntry<T> extends SpraySqlObject {
    SprayTuple._2<String, T> getConfig();
}
