package top.spray.engine.process.processor.data.analyse.direction;

import java.util.List;

public class SprayDataDirection {
    public static final SprayDataDirection INPUT = new SprayDataDirection("INPUT");

    public static final SprayDataDirection OUTPUT = new SprayDataDirection("OUTPUT");

    private static final List<SprayDataDirection> VALUES = List.of(INPUT, OUTPUT);


    private final String name;
    SprayDataDirection(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static SprayDataDirection of(String name) {
        return VALUES.stream()
                .filter(d -> d.getName().equals(name))
                .findAny().orElse(null);
    }
}
