package top.spray.engine.remoting.adapter.dubbo.constants;

public enum SprayDubboProtocol {
    DUBBO("dubbo", "dubbo"),
    TRIPLE("triple", "tri"),
    HTTP("http", "http"),
    ;
    private final String name;
    private final String value;
    SprayDubboProtocol(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public static SprayDubboProtocol nameOf(String name) {
        if (name != null) {
            name = name.toLowerCase();
            for (SprayDubboProtocol protocol : SprayDubboProtocol.values()) {
                if (protocol.name.equals(name)) {
                    return protocol;
                }
            }
        }
        return null;
    }

}
