package top.spray.core.global.thread;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;

public interface SprayThreadFactory extends ThreadFactory {
    @Override
    SprayThread newThread(@NotNull Runnable r);
}
