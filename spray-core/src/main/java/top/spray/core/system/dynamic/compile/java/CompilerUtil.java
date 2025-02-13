/*
 * Copyright 2014 Higher Frequency Trading
 *
 *       https://chronicle.software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.spray.core.system.dynamic.compile.java;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;
import top.spray.common.tools.Sprays;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * This class support loading and debugging Java Classes dynamically.
 */
public enum CompilerUtil {
    ; // none
    private static final Logger LOGGER = LoggerFactory.getLogger(CompilerUtil.class);
    private static final Method DEFINE_CLASS_METHOD;
    public static JavaCompiler s_compiler;
    public static StandardJavaFileManager s_standardJavaFileManager;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            Unsafe u = (Unsafe) theUnsafe.get(null);
            DEFINE_CLASS_METHOD = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
            try {
                Field f = AccessibleObject.class.getDeclaredField("override");
                long offset = u.objectFieldOffset(f);
                u.putBoolean(DEFINE_CLASS_METHOD, offset, true);
            } catch (NoSuchFieldException e) {
                DEFINE_CLASS_METHOD.setAccessible(true);
            }
        } catch (NoSuchMethodException | IllegalAccessException | NoSuchFieldException e) {
            throw new AssertionError(e);
        }
    }

    static {
        reset();
    }

    private static void reset() {
        s_compiler = ToolProvider.getSystemJavaCompiler();
        if (s_compiler == null) {
            try {
                Class<?> javacTool = Class.forName("com.sun.tools.javac.api.JavacTool");
                Method create = javacTool.getMethod("create");
                s_compiler = (JavaCompiler) create.invoke(null);
                s_standardJavaFileManager = s_compiler.getStandardFileManager(null, null, null);
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        }
    }


    /**
     * Add a directory to the class path for compiling.  This can be required with custom
     *
     * @param dir to add.
     * @return whether the directory was found, if not it is not added either.
     */
    public static boolean addClassPath(@NotNull String dir) {
        File file = new File(dir);
        if (file.exists()) {
            String path;
            try {
                path = file.getCanonicalPath();
            } catch (IOException ignored) {
                path = file.getAbsolutePath();
            }
            if (!Arrays.asList(System.getProperty(Sprays.SystemPropertyKey_ClassPath).split(File.pathSeparator)).contains(path)) {
                System.setProperty(Sprays.SystemPropertyKey_ClassPath,
                        System.getProperty(Sprays.SystemPropertyKey_ClassPath) + File.pathSeparator + path);
            }
        } else {
            return false;
        }
        reset();
        return true;
    }

    /**
     * Define a class for byte code.
     *
     * @param className expected to load.
     * @param bytes     of the byte code.
     */
    public static void defineClass(@NotNull String className, @NotNull byte[] bytes) {
        defineClass(Thread.currentThread().getContextClassLoader(), className, bytes);
    }

    /**
     * Define a class for byte code.
     *
     * @param classLoader to load the class into.
     * @param className   expected to load.
     * @param bytes       of the byte code.
     */
    public static Class<?> defineClass(@Nullable ClassLoader classLoader, @NotNull String className, @NotNull byte[] bytes) {
        try {
            return (Class) DEFINE_CLASS_METHOD.invoke(classLoader, className, bytes, 0, bytes.length);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
            throw new AssertionError(e.getCause());
        }
    }

    private static String readText(@NotNull String resourceName, String charsetName) throws IOException {
        if (resourceName.startsWith("="))
            return resourceName.substring(1);
        StringWriter sw = new StringWriter();
        Reader isr = new InputStreamReader(getInputStream(resourceName, charsetName), charsetName);
        try {
            char[] chars = new char[8 * 1024];
            int len;
            while ((len = isr.read(chars)) > 0)
                sw.write(chars, 0, len);
        } finally {
            close(isr);
        }
        return sw.toString();
    }

    @NotNull
    private static String decode(@NotNull byte[] bytes, String charsetName) {
        try {
            return new String(bytes, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    @Nullable
    @SuppressWarnings("ReturnOfNull")
    private static byte[] readBytes(@NotNull File file) {
        if (!file.exists()) return null;
        long len = file.length();
        if (len > Runtime.getRuntime().totalMemory() / 10)
            throw new IllegalStateException("Attempted to read large file " + file + " was " + len + " bytes.");
        byte[] bytes = new byte[(int) len];
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new FileInputStream(file));
            dis.readFully(bytes);
        } catch (IOException e) {
            close(dis);
            LOGGER.warn("Unable to read {}", file, e);
            throw new IllegalStateException("Unable to read file " + file, e);
        }

        return bytes;
    }

    private static void close(@Nullable Closeable closeable) {
        if (closeable != null)
            try {
                closeable.close();
            } catch (IOException e) {
                LOGGER.trace("Failed to close {}", closeable, e);
            }
    }

    public static boolean writeText(@NotNull File file, @NotNull String text, String charsetName) {
        return writeBytes(file, encode(text, charsetName), charsetName);
    }

    @NotNull
    private static byte[] encode(@NotNull String text, String charsetName) {
        try {
            return text.getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static boolean writeBytes(@NotNull File file, @NotNull byte[] bytes, String charsetName) {
        File parentDir = file.getParentFile();
        if (!parentDir.isDirectory() && !parentDir.mkdirs())
            throw new IllegalStateException("Unable to create directory " + parentDir);
        // only write to disk if it has changed.
        File bak = null;
        if (file.exists()) {
            byte[] bytes2 = readBytes(file);
            if (Arrays.equals(bytes, bytes2))
                return false;
            bak = new File(parentDir, file.getName() + ".bak");
            file.renameTo(bak);
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bytes);
        } catch (IOException e) {
            close(fos);
            LOGGER.warn("Unable to write {} as {}", file, decode(bytes, charsetName), e);
            file.delete();
            if (bak != null)
                bak.renameTo(file);
            throw new IllegalStateException("Unable to write " + file, e);
        }
        return true;
    }

    @NotNull
    private static InputStream getInputStream(@NotNull String filename, String charsetName) throws FileNotFoundException {
        if (filename.isEmpty()) throw new IllegalArgumentException("The file name cannot be empty.");
        if (filename.charAt(0) == '=') return new ByteArrayInputStream(encode(filename.substring(1), charsetName));
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = contextClassLoader.getResourceAsStream(filename);
        if (is != null) return is;
        InputStream is2 = contextClassLoader.getResourceAsStream('/' + filename);
        if (is2 != null) return is2;
        return new FileInputStream(filename);
    }
}
