package top.spray.core.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SprayClassLoader extends URLClassLoader {
    private Map<String, Class<?>> loadedClassMap = new ConcurrentHashMap<>();
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
                    return findClass(name);
                } catch (ClassNotFoundException e) {
                    // maybe is system class, try parents delegate
                    return super.loadClass(name, false);
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

    public void destroy() throws IOException {
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
}
