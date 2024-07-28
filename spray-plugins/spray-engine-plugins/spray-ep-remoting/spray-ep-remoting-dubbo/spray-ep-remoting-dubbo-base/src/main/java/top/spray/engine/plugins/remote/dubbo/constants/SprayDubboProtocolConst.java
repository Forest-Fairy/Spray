package top.spray.engine.plugins.remote.dubbo.constants;

public enum SprayDubboProtocolConst {
    DUBBO("dubbo", "dubbo"),
    TRIPLE("triple", "tri"),
    HTTP("http", "http"),
    ;
    private final String name;
    private final String value;
    SprayDubboProtocolConst(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public static SprayDubboProtocolConst nameOf(String name) {
        if (name != null) {
            name = name.toLowerCase();
            for (SprayDubboProtocolConst protocol : SprayDubboProtocolConst.values()) {
                if (protocol.name.equals(name)) {
                    return protocol;
                }
            }
        }
        return null;
    }

    public static SprayDubboProtocolConst parseWithUrl(String url) {
        if (url != null) {
            url = url.toLowerCase();
            for (SprayDubboProtocolConst protocol : SprayDubboProtocolConst.values()) {
                if (url.startsWith(protocol.value)) {
                    return protocol;
                }
            }
        }
        return null;
    }

}
