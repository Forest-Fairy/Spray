package top.spray.engine.process.infrastructure.listen;

public interface SprayListenEventReceiveResult {
    String eventId();
    Type getType();
    String getMessage();
    long receiveTime();
    long returnTime();
    boolean canRetry();

    static SprayListenEventReceiveResult NotInterested(String eventId, long receiveTime) {
        return new Type.CommonResult(eventId, receiveTime, "listener can not receive such event", Type.NOT_INTERESTED, false);
    }

    static SprayListenEventReceiveResult Forward(String eventId, long receiveTime, String message) {
        return new Type.CommonResult(eventId, receiveTime, message, Type.FORWARD, false);
    }

    static SprayListenEventReceiveResult Break(String eventId, long receiveTime, String message) {
        return new Type.CommonResult(eventId, receiveTime, message, Type.BREAK, false);
    }


    enum Type {
        NOT_INTERESTED,
        FORWARD,
        BREAK,
//        SUCCESS_FORWARD,
//        FINISHED_BREAK,
//        FAILED_FORWARD,
//        FAILURE_BREAK,


        ;
        public static class CommonResult implements SprayListenEventReceiveResult {
            private final String eventId;
            private final long receiveTime;
            private final String message;
            private final long returnTime;
            private final Type resultType;
            private final boolean canRetry;

            CommonResult(String eventId, long receiveTime, String message, Type resultType, boolean canRetry) {
                this.eventId = eventId;
                this.receiveTime = receiveTime;
                this.message = message;
                this.resultType = resultType;
                this.canRetry = canRetry;
                this.returnTime = System.currentTimeMillis();
            }

            @Override
            public String eventId() {
                return eventId;
            }

            @Override
            public Type getType() {
                return resultType;
            }

            @Override
            public String getMessage() {
                return message;
            }

            @Override
            public long receiveTime() {
                return receiveTime;
            }

            @Override
            public long returnTime() {
                return returnTime;
            }

            @Override
            public boolean canRetry() {
                return canRetry;
            }
        }
    }


}
