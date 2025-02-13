package top.spray.db.connection.action;

public interface SprayDataActionResult<Action extends SprayDataAction<?, ?>> {
    Action getAction();
}
