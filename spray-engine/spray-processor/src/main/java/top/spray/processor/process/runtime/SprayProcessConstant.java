package top.spray.processor.process.runtime;

public class SprayProcessConstant {
    public static final SprayProcessConstant INSTANCE = new SprayProcessConstant();
    private SprayProcessConstant() {}

    public static class NameSpace {
        /** Separator should use $ that is fitting the grammar for most of the program languages */
        public static final String EXECUTOR_NAMESPACE = "SPRAY$EXECUTOR$NAMESPACE";
    }
}
