package top.spray.core.dynamic.compile.java;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.spray.common.tools.Sprays;
import top.spray.core.global.config.util.SpraySystemConfigurations;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

public class SprayJavaCompiler {
    private static final Logger LOG = LoggerFactory.getLogger(SprayJavaCompiler.class);
    private static final List<String> DEFAULT_OPTIONS = Arrays.asList(
            "-g", "-nowarn", "-encoding", "UTF-8", "-XDuseUnsharedTable", "-XDuseJavaUtilZip");
    public static final SprayJavaCompiler DEFAULT = new SprayJavaCompiler(
            new File(new File(SpraySystemConfigurations.sprayProjectDir()), "compiler/java/sourceCode/"),
            new File(new File(SpraySystemConfigurations.sprayProjectDir()), "compiler/java/classes/"));

    private final Map<ClassLoader, Map<String, Result>> loadedClassesMap = Collections.synchronizedMap(new WeakHashMap<>());
    private final Map<ClassLoader, SprayJavaFileManager> fileManagerMap = Collections.synchronizedMap(new WeakHashMap<>());
    public Function<StandardJavaFileManager, SprayJavaFileManager> fileManagerOverride;

    @Nullable
    private final File sourceDir;
    @Nullable
    private final File classDir;
    @NotNull
    private final List<String> options;

    private final ConcurrentMap<String, JavaFileObject> javaFileObjects = new ConcurrentHashMap<>();

    public SprayJavaCompiler(@Nullable File sourceDir, @Nullable File classDir) {
        this(sourceDir, classDir, DEFAULT_OPTIONS);
    }

    public SprayJavaCompiler(@Nullable File sourceDir, @Nullable File classDir, @NotNull List<String> options) {
        this.sourceDir = sourceDir;
        this.classDir = classDir;
        this.options = options;
    }


    public void close() {
        try {
            for (SprayJavaFileManager fileManager : fileManagerMap.values()) {
                fileManager.close();
            }
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public Result loadFromJava(@NotNull String className, @NotNull String javaCode) throws ClassNotFoundException {
        return loadFromJava(getClass().getClassLoader(), className, javaCode);
    }

    public Result loadFromJava(@NotNull ClassLoader classLoader,
                               @NotNull String className,
                               @NotNull String javaCode) {
        Result result = null;
        Map<String, Result> loadedClasses;
        synchronized (loadedClassesMap) {
            loadedClasses = loadedClassesMap.get(classLoader);
            if (loadedClasses == null)
                loadedClassesMap.put(classLoader, loadedClasses = new LinkedHashMap<>());
            else
                result = loadedClasses.get(className);
        }
        if (result != null) {
            return result;
        }

        StringWriter errorOut = new StringWriter();
        PrintWriter printWriter = new PrintWriter(errorOut);
        SprayJavaFileManager fileManager = fileManagerMap.get(classLoader);
        if (fileManager == null) {
            StandardJavaFileManager standardJavaFileManager = CompilerUtil.s_compiler.getStandardFileManager(null, null, null);
            fileManager = createFileManager(standardJavaFileManager);
            fileManagerMap.put(classLoader, fileManager);
        }
        final Map<String, byte[]> compiled = compileFromJava(className, javaCode, printWriter, fileManager);
        for (Map.Entry<String, byte[]> entry : compiled.entrySet()) {
            String className2 = entry.getKey();
            synchronized (loadedClassesMap) {
                if (loadedClasses.containsKey(className2))
                    continue;
            }
            byte[] bytes = entry.getValue();
            if (classDir != null) {
                String filename = className2.replaceAll("\\.", '\\' + File.separator) + ".class";
                boolean changed = CompilerUtil.writeBytes(new File(classDir, filename), bytes, Sprays.UTF_8.name());
                if (changed) {
                    LOG.info("Updated {} in {}", className2, classDir);
                }
            }

            synchronized (className2.intern()) { // To prevent duplicate class definition error
                synchronized (loadedClassesMap) {
                    if (loadedClasses.containsKey(className2))
                        continue;
                }

                Class<?> clazz2 = CompilerUtil.defineClass(classLoader, className2, bytes);
                synchronized (loadedClassesMap) {
                    loadedClasses.put(className2, new Result(clazz2));
                }
            }
        }
        synchronized (loadedClassesMap) {
            try {
                loadedClasses.put(className, result = new Result(classLoader.loadClass(className)));
            } catch (ClassNotFoundException classNotFoundException) {
                result = new Result(classNotFoundException, "failed");
            }
        }
        return result;
    }

    @NotNull
    Map<String, byte[]> compileFromJava(@NotNull String className,
                                        @NotNull String javaCode,
                                        PrintWriter writer,
                                        SprayJavaFileManager fileManager) {
        String filename = className.replaceAll("\\.", '\\' + File.separator) + ".java";
        File dir = sourceDir == null ? new File(SpraySystemConfigurations.sprayProjectDir()) : sourceDir;
        File file = new File(dir, filename);
        CompilerUtil.writeText(file, javaCode, Sprays.UTF_8.name());
        Iterable<? extends JavaFileObject> compilationUnits = CompilerUtil.s_standardJavaFileManager.getJavaFileObjects(file);
        // reuse the same file manager to allow caching of jar files
        boolean ok = CompilerUtil.s_compiler.getTask(writer, fileManager, new DiagnosticListener<JavaFileObject>() {
            @Override
            public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
                if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                    writer.println(diagnostic);
                }
            }
        }, options, null, compilationUnits).call();

        if (!ok) {
            // compilation error, so we want to exclude this file from future compilation passes
            if (sourceDir == null) {
                javaFileObjects.remove(className);
            }
            // nothing to return due to compiler error
            return Collections.emptyMap();
        } else {
            return fileManager.getAllBuffers();
        }
    }

    private @NotNull SprayJavaFileManager createFileManager(StandardJavaFileManager fm) {
        return fileManagerOverride != null
                ? fileManagerOverride.apply(fm)
                : new SprayJavaFileManager(fm);
    }


//    public static Result<?> compile(String source, String fullClassName, ClassLoader parentClassloader, String classPathOpt, Locale locale, Charset charset) {
//        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//
//        StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(null, null, null);
//        LinkedList<String> options = new LinkedList();
//        if (classPathOpt != null && !classPathOpt.trim().isEmpty()) {
//            options.add("-classpath");
//            options.add(classPathOpt.trim());
//        }
//        options.add("-encoding");
//        options.add("UTF-8");
//        options.add("-XDuseUnsharedTable");
//        options.add("-XDuseJavaUtilZip");
//        StringWriter errorOut = new StringWriter();
//        DiagnosticCollector<Object> diagnosticCollector = new DiagnosticCollector<>();
//        try {
//            if (parentClassloader instanceof URLClassLoader urlClassLoader) {
//                wrapClassPath(standardFileManager, urlClassLoader);
//            }
//            compiler.getTask(errorOut, standardFileManager, diagnosticCollector, options, null, )
//        }
//    }
//
//    private static void wrapClassPath(StandardJavaFileManager standardFileManager, URLClassLoader urlClassLoader) throws IOException {
//        URL[] urls = urlClassLoader.getURLs();
//        List<File> files = new ArrayList<>(urls.length);
//        for (URL url : urls) {
//            files.add(new File(url.getFile()));
//        }
//        standardFileManager.setLocation(StandardLocation.CLASS_PATH, files);
//    }


    public static class Result {
        private final Class<?> target;
        private final Throwable throwable;
        private final String errorResult;
        private Result(Class<?> target) {
            this.target = target;
            this.throwable = null;
            this.errorResult = null;
        }
        private Result(Throwable throwable, String errorResult) {
            this.target = null;
            this.throwable = throwable;
            this.errorResult = errorResult;
        }
        public boolean isSuccess() {
            return this.target != null;
        }

        public Class<?> getTarget() {
            return target;
        }

        public Throwable getThrowable() {
            return throwable;
        }

        public String getErrorResult() {
            return errorResult;
        }

    }
}
