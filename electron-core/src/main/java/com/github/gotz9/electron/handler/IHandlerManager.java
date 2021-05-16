package com.github.gotz9.electron.handler;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.util.ClassUtils.CLASS_FILE_SUFFIX;

public class IHandlerManager {

    private final Path handlerClassPath;

    private HandlerClassLoader handlerClassLoader;

    private Map<String, IHandler> handlerNameMap = new ConcurrentHashMap<>();

    private Map<Integer, IHandler> handlerMessageMap = new ConcurrentHashMap<>();

    public IHandlerManager(String handlerClassPath) {
        this.handlerClassPath = Paths.get(handlerClassPath);
        this.handlerClassLoader = new HandlerClassLoader(this.handlerClassPath);
    }

    /**
     * 加载 Handler
     */
    public void loadHandler() throws IOException, ClassNotFoundException {
        Set<String> classNames = scanClassFiles(handlerClassPath);

        for (String className : classNames) {
            createAndRegisterHandler(className);
        }
    }

    public IHandler getHandler(int id) {
        return handlerMessageMap.get(id);
    }

    /**
     * 创建 Handler
     *
     * @param className
     * @throws ClassNotFoundException
     */
    private void createAndRegisterHandler(String className) throws ClassNotFoundException {
        Class<?> handlerClass = handlerClassLoader.loadClass(className);

        if (!IHandler.class.isAssignableFrom(handlerClass)) {
            // 继承自 IHandler
            return;
        }

        IHandler.MessageHandler annotation = handlerClass.getAnnotation(IHandler.MessageHandler.class);
        if (annotation == null) {
            // 没有注解, 跳过加载
            return;
        }

        IHandler handler;
        try {
            handler = (IHandler) handlerClass.newInstance();
        } catch (Exception e) {
            // 创建实例异常
            throw new RuntimeException(e);
        }

        handlerNameMap.put(className, handler);
        handlerMessageMap.put(annotation.value(), handler);
    }

    /**
     * 扫描 .class 文件, 返回全限定类名集合
     *
     * @throws IOException
     */
    private static Set<String> scanClassFiles(Path root) throws IOException {
        Path path = root.toAbsolutePath();
        int nameCount = path.getNameCount();

        Set<String> paths = new HashSet<>();
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                final File f = file.toFile();
                if (f.canRead() && f.getName().endsWith(CLASS_FILE_SUFFIX)) {
                    String fileRelativePath = file.subpath(nameCount, file.getNameCount()).toString().replace(File.separator, ".");
                    fileRelativePath = fileRelativePath.substring(0, fileRelativePath.length() - CLASS_FILE_SUFFIX.length());
                    paths.add(fileRelativePath);
                }

                return FileVisitResult.CONTINUE;
            }
        });

        return paths;
    }

}
