package top.spray.engine.coordinate.type;

public enum SprayCoordinatorRemoteType {
    /** the coordinator execute all executor in local */
    LOCAL(0, "Local"),
    /** executor send instruction to coordinator
     *  and send data to next executor
     */
    DIRECT(1, "Direct"),
    /** executor send status to coordinator and
     *  the coordinator send an instruction back
     *  then the executor send data to next executor
     */
    FORWARD(2, "Forward"),
    /** executor send data to coordinator then
     *  the coordinator send data to next executor
     */
    RELAY(3, "Relay"),
    /** decided by each executor */
    DYNAMIC(4, "Dynamic");
    ;
    private final int code;
    private final String typeName;

    SprayCoordinatorRemoteType(int code, String typeName) {
        this.code = code;
        this.typeName = typeName;
    }
    public int getCode() {
        return code;
    }
    public String getTypeName() {
        return typeName;
    }
    public static SprayCoordinatorRemoteType typeOf(int code) {
        for (SprayCoordinatorRemoteType type : SprayCoordinatorRemoteType.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown SprayCoordinatorRemoteType code: " + code);
    }
    public static SprayCoordinatorRemoteType typeOf(String typeName) {
        return SprayCoordinatorRemoteType.valueOf(typeName.toUpperCase());
    }
}
