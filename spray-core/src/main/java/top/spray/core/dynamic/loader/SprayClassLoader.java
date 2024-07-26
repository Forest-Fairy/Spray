package top.spray.core.dynamic.loader;

import cn.hutool.core.bean.BeanUtil;
import jdk.internal.access.SharedSecrets;
import jdk.internal.loader.Resource;
import jdk.internal.loader.URLClassPath;
import jdk.internal.perf.PerfCounter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.security.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class SprayClassLoader extends URLClassLoader {
    private static final SprayClassLoader default_loader;
    static {
        URL location = SprayClassLoader.class.getProtectionDomain().getCodeSource().getLocation();
        URL[] urls;
        if (location.getPath().contains(".jar")) {
            urls = new URL[]{location};
        } else {
            urls = new URL[0];
        }
        default_loader = new SprayClassLoader(urls, ClassLoader.getSystemClassLoader());
    }

    /* The search path for classes and resources */
    private final URLClassPath urlClassPath;

    /* The context to be used when loading classes and resources */
    @SuppressWarnings("removal")
    private final AccessControlContext accessControlContext;

    private Map<String, Class<?>> loadedClassMap = new ConcurrentHashMap<>();

    private List<>

    public SprayClassLoader(String jarFiles) {
        this(jarFiles, Thread.currentThread().getContextClassLoader());
    }
    public SprayClassLoader(String jarFiles, ClassLoader parent) {
        this(StringUtils.isBlank(jarFiles) ? new URL[0] :
                convertFilesToUrls(convertPathsToFiles(jarFiles)),
                parent, null);
    }
    public SprayClassLoader(URL[] urls) {
        this(urls, Thread.currentThread().getContextClassLoader());
    }
    public SprayClassLoader(URL[] urls, ClassLoader parent) {
        this(urls, parent, null);
    }

    public SprayClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
        this.accessControlContext = (AccessControlContext) BeanUtil.getFieldValue(this, "acc");
        this.urlClassPath = (URLClassPath) BeanUtil.getFieldValue(this, "ucp");
    }

    public void addURLs(String files) {
        addURLs(convertPathsToFiles(files));
    }
    public void addURLs(Collection<File> fileSet) {
        addURLs(convertFilesToUrls(fileSet));
    }
    public void addURLs(URL... urls) {
        for (URL url : urls) {
            this.addURL(url);
        }
    }

    private static Collection<File> convertPathsToFiles(String paths) {
        if (StringUtils.isBlank(paths)) {
            return new ArrayList<>();
        }
        paths = paths.replace("\\", "/").trim();
        String[] pathsArr = StringUtils.split(paths, ",");
        if (pathsArr.length == 1 && paths.endsWith("/*")) {
            // all files in the path
            paths = paths.substring(0, paths.length() - 2);
            File pathDir = new File(paths);
            if (!pathDir.exists() || !pathDir.isDirectory()) {
                throw new RuntimeException("jar dir not exists: " + paths);
            }
            // collect all the jar files in path
            return listAllFilesInDir(pathDir);
        }
        return Arrays.stream(pathsArr)
                .map(p -> {
                    File file = new File(p);
                    if (!file.exists()) {
                        throw new RuntimeException("jar file not exists: " + p);
                    } else {
                        return file;
                    }
                })
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    private static URL[] convertFilesToUrls(Collection<File> fileSet) {
        return fileSet.stream()
                // only jar file can be added
                .filter(x -> x.getName().endsWith(".jar"))
                .map(x -> {
                    try {
                        return x.toURI().toURL();
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toArray(URL[]::new);
    }
    private static Collection<File> listAllFilesInDir(File dir) {
        List<File> allFiles = new ArrayList<>();
        if (dir != null && dir.exists() && dir.isDirectory() && dir.listFiles() != null) {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    allFiles.addAll(listAllFilesInDir(file));
                } else {
                    allFiles.add(file);
                }
            }
        }
        return allFiles;
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            Class<?> loadedClass = findLoadedClass(name);

            if (loadedClass == null) {
                try {
                    // try to use this classloader to load
                    loadedClass = findClass(name);
                } catch (ClassNotFoundException e) {
                    // maybe is system class, try parents delegate
                    loadedClass = super.loadClass(name, false);
                }
            } else if (resolve) {
                resolveClass(loadedClass);
            }
            return loadedClass;
        }
    }

    @Override
    public URL getResource(String name) {
        // first, try and find it via the URLClassloader
        URL urlClassLoaderResource = findResource(name);

        if (urlClassLoaderResource != null) {
            return urlClassLoaderResource;
        }

        // delegate to super
        return super.getResource(name);
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        // first get resources from URLClassloader
        Enumeration<URL> urlClassLoaderResources = findResources(name);
        final List<URL> result = new ArrayList<>();
        while (urlClassLoaderResources.hasMoreElements()) {
            result.add(urlClassLoaderResources.nextElement());
        }
        // get parent urls
        Enumeration<URL> parentResources = getParent().getResources(name);
        while (parentResources.hasMoreElements()) {
            result.add(parentResources.nextElement());
        }

        return new Enumeration<URL>() {
            Iterator<URL> iter = result.iterator();
            public boolean hasMoreElements() {
                return iter.hasNext();
            }
            public URL nextElement() {
                return iter.next();
            }
        };
    }

    @Override
    public void close() throws IOException {
        for (Map.Entry<String, Class<?>> entry : loadedClassMap.entrySet()) {
            try {
                // invoke a static destroy method
                Method destroy = entry.getValue().getDeclaredMethod("destroy");
                destroy.invoke(null);
            } catch (Exception ignored) {
                // nothing need to do with
            }
        }
        loadedClassMap.clear();
        // 从其父类加载器的加载器层次结构中移除该类加载器
        super.close();
    }

    public static SprayClassLoader getDefault() {
        return default_loader;
    }

    // JDK UrlClassLoader Code

    /**
     * Finds and loads the class with the specified name from the URL search
     * path. Any URLs referring to JAR files are loaded and opened as needed
     * until the class is found.
     *
     * @param     name the name of the class
     * @return    the resulting class
     * @throws    ClassNotFoundException if the class could not be found,
     *            or if the loader is closed.
     * @throws    NullPointerException if {@code name} is {@code null}.
     */
    protected Class<?> findClass(final String name)
            throws ClassNotFoundException
    {
        final Class<?> result;
        try {
            result = AccessController.doPrivileged(
                    new PrivilegedExceptionAction<>() {
                        public Class<?> run() throws ClassNotFoundException {
                            String path = name.replace('.', '/').concat(".class");
                            Resource res = urlClassPath.getResource(path, false);
                            if (res != null) {
                                try {
                                    return defineClass(name, res);
                                } catch (IOException e) {
                                    throw new ClassNotFoundException(name, e);
                                } catch (ClassFormatError e2) {
                                    if (res.getDataError() != null) {
                                        e2.addSuppressed(res.getDataError());
                                    }
                                    throw e2;
                                }
                            } else {
                                return null;
                            }
                        }
                    }, accessControlContext);
        } catch (java.security.PrivilegedActionException pae) {
            throw (ClassNotFoundException) pae.getException();
        }
        if (result == null) {
            throw new ClassNotFoundException(name);
        }
        return result;
    }
    /**
     * Defines a Class using the class bytes obtained from the specified
     * Resource. The resulting Class must be resolved before it can be
     * used.
     */
    private Class<?> defineClass(String name, Resource res) throws IOException {
        long t0 = System.nanoTime();
        int i = name.lastIndexOf('.');
        URL url = res.getCodeSourceURL();
        if (i != -1) {
            String pkgname = name.substring(0, i);
            // Check if package already loaded.
            Manifest man = res.getManifest();
            if (getAndVerifyPackage(pkgname, man, url) == null) {
                try {
                    if (man != null) {
                        definePackage(pkgname, man, url);
                    } else {
                        definePackage(pkgname, null, null, null, null, null, null, null);
                    }
                } catch (IllegalArgumentException iae) {
                    // parallel-capable class loaders: re-verify in case of a
                    // race condition
                    if (getAndVerifyPackage(pkgname, man, url) == null) {
                        // Should never happen
                        throw new AssertionError("Cannot find package " +
                                pkgname);
                    }
                }
            }
        }
        // Now read the class bytes and define the class
        java.nio.ByteBuffer bb = res.getByteBuffer();
        if (bb != null) {
            // Use (direct) ByteBuffer:
            CodeSigner[] signers = res.getCodeSigners();
            CodeSource cs = new CodeSource(url, signers);
            PerfCounter.getReadClassBytesTime().addElapsedTimeFrom(t0);

            return defineClass(name, bb, cs);
        } else {
            byte[] b = res.getBytes();
            // must read certificates AFTER reading bytes.
            CodeSigner[] signers = res.getCodeSigners();
            CodeSource cs = new CodeSource(url, signers);
            PerfCounter.getReadClassBytesTime().addElapsedTimeFrom(t0);

            return defineClass(name, b, 0, b.length, cs);
        }
    }
    /*
     * Retrieve the package using the specified package name.
     * If non-null, verify the package using the specified code
     * source and manifest.
     */
    private Package getAndVerifyPackage(String pkgname,
                                        Manifest man, URL url) {
        Package pkg = getDefinedPackage(pkgname);
        if (pkg != null) {
            // Package found, so check package sealing.
            if (pkg.isSealed()) {
                // Verify that code source URL is the same.
                if (!pkg.isSealed(url)) {
                    throw new SecurityException(
                            "sealing violation: package " + pkgname + " is sealed");
                }
            } else {
                // Make sure we are not attempting to seal the package
                // at this code source URL.
                if ((man != null) && isSealed(pkgname, man)) {
                    throw new SecurityException(
                            "sealing violation: can't seal package " + pkgname +
                                    ": already loaded");
                }
            }
        }
        return pkg;
    }

    /**
     * Returns true if the specified package name is sealed according to the
     * given manifest.
     *
     * @throws SecurityException if the package name is untrusted in the manifest
     */
    private boolean isSealed(String name, Manifest man) {
        Attributes attr = SharedSecrets.javaUtilJarAccess()
                .getTrustedAttributes(man, name.replace('.', '/').concat("/"));
        String sealed = null;
        if (attr != null) {
            sealed = attr.getValue(Attributes.Name.SEALED);
        }
        if (sealed == null) {
            if ((attr = man.getMainAttributes()) != null) {
                sealed = attr.getValue(Attributes.Name.SEALED);
            }
        }
        return "true".equalsIgnoreCase(sealed);
    }



}
