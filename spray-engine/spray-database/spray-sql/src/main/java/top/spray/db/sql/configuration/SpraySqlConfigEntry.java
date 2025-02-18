package top.spray.db.sql.configuration;

import top.spray.common.tools.tuple.SprayTuples;
import top.spray.db.sql.objects.SpraySqlObject;

public interface SpraySqlConfigEntry<T> extends SpraySqlObject {
    SprayTuples._2<String, T> getConfig();
}
