package top.spray.core.engine.exception;

import org.slf4j.Logger;
import top.spray.core.engine.meta.SprayBaseMeta;
import top.spray.core.util.ThreadUtil;

public class SprayNotSupportError extends SprayMetaError {
    public SprayNotSupportError(SprayBaseMeta<?> meta, Throwable cause) {
        super(meta == null ? NoneExistMeta.INSTANCE : meta, "not support", cause);
    }

    private static class NoneExistMeta implements SprayBaseMeta<NoneExistMeta> {
        static final NoneExistMeta INSTANCE = new NoneExistMeta();
        @Override
        public String getId() {
            return "no meta Id defined";
        }

        @Override
        public String getName() {
            return "no meta name defined";
        }

        @Override
        public String transactionId() {
            return ThreadUtil.getThreadTransactionId();
        }

        @Override
        public Logger logger() {
            return null;
        }

        @Override
        public String jarFiles() {
            return "";
        }

        @Override
        public boolean isAsync() {
            return false;
        }
    }
}
