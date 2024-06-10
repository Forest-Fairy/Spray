package top.spray.engine.base.result;

public enum SprayStepExecuteResult {
    SUCCESS("success", "成功"),
    ERROR("error", "异常"),
    SKIP("skip", "跳过"),
    ;

    private interface ResultHandler {
        // TODO 需要一个接口类定义一个状态信息处理方法，将结果实例传入.
    }

    private final String name;

    private final String chsName;

    SprayStepExecuteResult(String name, String chsName) {
        this.name = name;
        this.chsName = chsName;
    }

    public String getName() {
        return name;
    }
    public String getChsName() {
        return chsName;
    }
}
