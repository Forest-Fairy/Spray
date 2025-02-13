package top.spray.db.connection.action;

import top.spray.common.bean.SprayClassUtil;
import top.spray.common.tools.SpraySynchronizeUtils;
import top.spray.core.global.exception.SprayCoreException;
import top.spray.core.global.prop.SprayUnsupportedOperation;
import top.spray.db.connection.connection.SprayConnection;
import top.spray.db.connection.exception.SpraySqlException;

import java.util.HashMap;
import java.util.Map;

public abstract class SprayActionHandler<
        Connection extends SprayConnection<?>,
        Action extends SprayDataAction<Result, ?>,
        Result extends SprayDataActionResult<Action>> {
    private static final Map<Class<? extends SprayConnection>, Map<SprayDataAction.Type, SprayActionHandler>> HANDLERS = new HashMap<>();
    public static <Handler extends SprayActionHandler> Handler getInstance(
            Class<? extends SprayConnection> connType, SprayDataAction action) {
        Map<SprayDataAction.Type, SprayActionHandler> handlerMap = HANDLERS.get(connType);
        return handlerMap == null ? null : (Handler) handlerMap.get(action.getActionType());
    }
    private static void Register(SprayActionHandler handler, SprayDataAction.Type actionType) {
        Class<? extends SprayConnection> connType =
                (Class<? extends SprayConnection>) SprayClassUtil.getClassGenericType(handler.getClass(), 0);
        Map<SprayDataAction.Type, SprayActionHandler> handlerMap = SpraySynchronizeUtils
                .synchronizeGet(HANDLERS, connType, HashMap::new);
        SprayActionHandler existHandler = handlerMap.get(actionType);
        if (existHandler != null) {
            throw new SprayCoreException(String.format("duplicated ActionHandler exist: %s, new register: %s",
                    existHandler.getClass().getName(), handler.getClass().getName()));
        }
        handlerMap.put(actionType, handler);
    }
    protected void registry(SprayDataAction.Type actionType) {
        Register(this, actionType);
    }
    public final Result handle(Connection connection, Action action) throws Exception {
        try {
            String parameterizedSql = action.toParameterizedSql().trim();
            validateAction(parameterizedSql, connection, action);
            Result result = doHandle(parameterizedSql, connection, action);
            action.callBack(result, null);
            return result;
        } catch (Throwable throwable) {
            action.callBack(null, throwable);
            return null;
        }
    }
    protected abstract void validateAction(String parameterizedSql, Connection connection, Action action) throws Exception;
    protected abstract Result doHandle(String parameterizedSql, Connection connection, Action action) throws Exception;
    protected String GetFirstToken(String sql) {
        return sql.substring(0, sql.indexOf(" "));
    }
    protected <T> T unsupported(String msg) {
        return SprayUnsupportedOperation.unsupported(SpraySqlException::new, msg);
    }
}
