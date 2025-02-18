package top.spray.core.dynamic;


import top.spray.common.tools.loop.SprayLooper;
import top.spray.core.dynamic.compile.java.SprayJavaClassObject;
import top.spray.core.dynamic.compile.java.SprayJavaCompiler;
import top.spray.core.dynamic.listener.SprayClassLoaderListener;

import java.io.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class SprayClassLoader extends ClassLoader implements AutoCloseable {
    private List<SprayClassLoaderListener> loaderListeners;
    private final Map<String, SprayJavaClassObject<?>> classes;
    private SprayJavaCompiler compiler;

    public SprayClassLoader() {
        this(null);
    }

    public SprayClassLoader(ClassLoader parentClassloader) {
        super(parentClassloader == null ? getSystemClassLoader() : parentClassloader);
        this.classes = new HashMap<>();
        this.loaderListeners = new LinkedList<>();
    }

    public void setCompiler(SprayJavaCompiler compiler) {
        this.compiler = compiler;
    }

    public SprayClassLoader addListener(SprayClassLoaderListener loaderListener, SprayClassLoaderListener... loaderListeners) {
        this.loaderListeners.add(loaderListener);
        if (loaderListeners != null && loaderListeners.length > 0) {
            this.loaderListeners.addAll(Arrays.asList(loaderListeners));
        }
        return this;
    }

    public Class<?> loadClass(String className) throws ClassNotFoundException {
        return findClass(className);
    }

    public Class<?> findClass(String className) throws ClassNotFoundException {
        Class<?> result = null;
        SprayJavaClassObject<?> fileObject = classes.get(className);
        if (fileObject != null) {
            result = fileObject.classObject();
        }
        if (result != null) {
            return result;
        }
        return findSystemClass(className);
    }

    public void addClass(SprayJavaClassObject<?> fileObject) {
        Objects.requireNonNull(fileObject, "fileObject should not be null!");
        try {
            if (findClass(fileObject.fullClassName()) != null) {
                throw new IllegalStateException("Class " + fileObject + " already exists!");
            }
        } catch (ClassNotFoundException ignored) {
        }
        if (fileObject.classObject() == null) {
            Class<?> loadedClass;
            if (fileObject.source() instanceof SprayJavaClassObject.Source.ClassFile classFileSource) {
                File classFile = classFileSource.get();
                try {
                    BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(classFile));
                    byte[] bytes = inputStream.readAllBytes();
                    loadedClass = defineClass(fileObject.fullClassName(), bytes, 0, bytes.length);
                } catch (Exception e) {
                    throw new RuntimeException("errored while adding class file " + classFileSource.get().getAbsolutePath(), e);
                }
            } else if (fileObject.source() instanceof SprayJavaClassObject.Source.Jar jarSource) {
                JarFile jar = jarSource.get();
                ZipEntry entry = jar.getEntry(fileObject.fullClassName() + ".class");
                try {
                    InputStream is = jar.getInputStream(entry);
                    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                    int nextValue = is.read();
                    while (-1 != nextValue) {
                        byteStream.write(nextValue);
                        nextValue = is.read();
                    }
                    byte[] classByte = byteStream.toByteArray();
                    loadedClass = defineClass(fileObject.fullClassName(), classByte, 0, classByte.length);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (fileObject.source() instanceof SprayJavaClassObject.Source.Code codeSource) {
                String code = codeSource.get();
                SprayJavaCompiler sprayJavaCompiler = this.compiler == null ? SprayJavaCompiler.DEFAULT : this.compiler;
                SprayJavaCompiler.Result compileResult = sprayJavaCompiler.loadFromJava(this, fileObject.fullClassName(), code);
                if (! compileResult.isSuccess()) {
                    throw new RuntimeException("unable to compile class " + fileObject + ": " + compileResult.getErrorResult(),
                            compileResult.getThrowable());
                }
                loadedClass = compileResult.getTarget();
            } else {
                loadedClass = null;
            }
            if (loadedClass == null) {
                throw new IllegalStateException("unable to load class " + fileObject);
            }
            SprayLooper.loopAndIgnoredException(this.loaderListeners,
                    listener -> listener.newClassDefined(this, loadedClass));
            fileObject.defineClass(loadedClass);
        }
        this.classes.put(fileObject.fullClassName(), fileObject);
    }

    public void addClassWithClassFile(String fullClassName, File classFile) {
        addClass(new SprayJavaClassObject<>(fullClassName, SprayJavaClassObject.Source.ClassFile.class, classFile));
    }
    public void addClass(String fullClassName, String sourceCode) {
        addClass(new SprayJavaClassObject<>(fullClassName, SprayJavaClassObject.Source.Code.class, sourceCode));
    }
    public void addClasses(File[] jarFiles, String... classNames) {
        for (File jarFile : jarFiles) {
            addClasses(jarFile, classNames);
        }
    }
    public void addClasses(File jarFile, String... classNames) {
        try {
            if (!jarFile.getName().toLowerCase().endsWith(".jar")) {
                throw new IllegalArgumentException("file " + jarFile + " is not a jar file!");
            }
            JarFile jar = new JarFile(jarFile.getAbsolutePath());
            Iterator<JarEntry> iterator = jar.entries().asIterator();
            Set<String> filter = classNames.length == 0 ? null : new HashSet<>(Arrays.asList(classNames));
            while (iterator.hasNext()) {
                JarEntry entry = iterator.next();
                if (entry.getRealName().endsWith(".class")) {
                    String fullClassName = entry.getRealName().substring(0, entry.getRealName().length() - 6);
                    if (filter == null || filter.contains(fullClassName)) {
                        addClass(new SprayJavaClassObject<>(fullClassName, SprayJavaClassObject.Source.Jar.class, jar));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        SprayLooper.loopAndIgnoredException(this.loaderListeners,
                listener -> listener.onClassLoaderClose(this));
    }

}