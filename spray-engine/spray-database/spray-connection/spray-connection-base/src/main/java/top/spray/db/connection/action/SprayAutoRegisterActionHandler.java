package top.spray.db.connection.action;

import top.spray.db.connection.connection.SprayConnection;

import java.util.stream.Stream;

public abstract class SprayAutoRegisterActionHandler<
        Connection extends SprayConnection<?>,
        Action extends SprayDataAction<Result, ?>,
        Result extends SprayDataActionResult<Action>>
        extends SprayActionHandler<Connection, Action, Result> {
    protected SprayAutoRegisterActionHandler() {
        Stream.of(types()).forEach(this::registry);
    }
    protected abstract SprayDataAction.Type[] types();
}
