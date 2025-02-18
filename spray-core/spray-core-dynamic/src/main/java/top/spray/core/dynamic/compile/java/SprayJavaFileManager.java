package top.spray.core.dynamic.compile.java;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SprayJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {
    private static final Logger logger = LoggerFactory.getLogger(SprayJavaFileManager.class);
    // synchronizing due to ConcurrentModificationException
    private final Map<String, CloseableByteArrayOutputStream> buffers = Collections.synchronizedMap(new LinkedHashMap<>());

    public SprayJavaFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    public JavaFileObject getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind) throws IOException {
        if (location == StandardLocation.CLASS_OUTPUT) {
            boolean success;
            final byte[] bytes;
            synchronized (buffers) {
                success = buffers.containsKey(className) && kind == JavaFileObject.Kind.CLASS;
                bytes = buffers.get(className).toByteArray();
            }
            if (success) {
                return new SimpleJavaFileObject(URI.create(className), kind) {
                    @NotNull
                    public InputStream openInputStream() {
                        return new ByteArrayInputStream(bytes);
                    }
                };
            }
        }
        return super.getJavaFileForInput(location, className, kind);
    }

    @NotNull
    public JavaFileObject getJavaFileForOutput(Location location, final String className, JavaFileObject.Kind kind, FileObject sibling) {
        return new SimpleJavaFileObject(URI.create(className), kind) {
            @NotNull
            public OutputStream openOutputStream() {
                // CloseableByteArrayOutputStream.closed is used to filter partial results from getAllBuffers()
                CloseableByteArrayOutputStream baos = new CloseableByteArrayOutputStream();

                // Reads from getAllBuffers() should be repeatable:
                // let's ignore compile result in case compilation of this class was triggered before
                buffers.putIfAbsent(className, baos);
                return baos;
            }
        };
    }

//    public void flush() {
//        // Do nothing
//    }


    public void clearBuffers() {
        buffers.clear();
    }

    @NotNull
    public Map<String, byte[]> getAllBuffers() {
        Map<String, byte[]> ret = new LinkedHashMap<>(buffers.size() * 2);
        Map<String, CloseableByteArrayOutputStream> compiledClasses = new LinkedHashMap<>(ret.size());

        synchronized (buffers) {
            compiledClasses.putAll(buffers);
        }

        for (Map.Entry<String, CloseableByteArrayOutputStream> e : compiledClasses.entrySet()) {
            try {
                // Await for compilation in case class is still being compiled in previous compiler run.
                e.getValue().closeFuture().get(30, TimeUnit.SECONDS);
            } catch (InterruptedException t) {
                Thread.currentThread().interrupt();
                logger.warn("Interrupted while waiting for compilation result [class=" + e.getKey() + "]");
                break;
            } catch (ExecutionException | TimeoutException t) {
                logger.warn("Failed to wait for compilation result [class=" + e.getKey() + "]", t);
                continue;
            }
            final byte[] value = e.getValue().toByteArray();
            ret.put(e.getKey(), value);
        }
        return ret;
    }


    public static class CloseableByteArrayOutputStream extends ByteArrayOutputStream {
        private final CompletableFuture<?> closeFuture = new CompletableFuture<>();

        @Override
        public void close() {
            closeFuture.complete(null);
        }

        public CompletableFuture<?> closeFuture() {
            return closeFuture;
        }
    }

}
