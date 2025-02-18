package top.spray.core.dynamic.compile.java;

import java.io.File;
import java.util.Objects;
import java.util.jar.JarFile;

public class SprayJavaClassObject<T> {
    private final String fullClassName;
    private final Source<T> source;
    private Class<?> classObject;

    public SprayJavaClassObject(String fullClassName, Class<? extends Source<T>> sourceClass, T t) {
        this.fullClassName = fullClassName;
        try {
            this.source = sourceClass.getConstructor(t.getClass()).newInstance(t);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public SprayJavaClassObject(String fullClassName, Source<T> source) {
        this.fullClassName = fullClassName;
        this.source = source;
    }


    public void defineClass(Class<?> classObject) {
        Objects.requireNonNull(classObject, "class definition should not be null!");
        this.classObject = classObject;
    }

    @Override
    public String toString() {
        return String.format("SprayClassObject{%s}", fullClassName());
    }

    public String fullClassName() {
        return fullClassName;
    }
    public Class<?> classObject() {
        return classObject;
    }
    public Source<T> source() {
        return source;
    }

    public abstract static class Source<T> implements AutoCloseable {
        public abstract static class Code extends Source<String> {
            public Code() {
                super("code");
            }
        }
        public abstract static class ClassFile extends Source<File> {
            public ClassFile() {
                super("class");
            }
        }
        public abstract static class Jar extends Source<JarFile> {
            public Jar() {
                super("jar");
            }

            @Override
            public void close() throws Exception {
                this.get().close();
            }
        }

        private final String name;
        private Source(String name) {
            this.name = name;
        }
        public String name() {
            return this.name;
        }
        public abstract T get();

        @Override
        public void close() throws Exception {
        }
    }


}
