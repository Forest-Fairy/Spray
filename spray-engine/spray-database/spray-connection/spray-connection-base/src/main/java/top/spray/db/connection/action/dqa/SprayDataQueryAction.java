package top.spray.db.connection.action.dqa;

import top.spray.db.connection.action.SprayDataAction;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface SprayDataQueryAction extends SprayDataAction<SprayDataQueryResult, SprayDataQueryType> {
    List<String> selectFields();
    Map<String, Function<Object, Object>> castors();
}
