package top.spray.engine.design.worker;

import top.spray.engine.design.event.model.SprayEventConsumer;

public class SprayEventWorker implements Runnable {
    private final SprayEventConsumer consumer;

    public SprayEventWorker(SprayEventConsumer consumer) {
        this.consumer = consumer;
    }
    @Override
    public void run() {
        try {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
                consumer.consume();
            }
        } catch (Exception e) {
            // TODO handle ex
            throw new RuntimeException(e);
        }
    }
}
