package com.github.gotz9.electron.handler;

import java.io.*;
import java.nio.file.Path;

import static com.github.gotz9.electron.utils.ElectronUtils.CLASS_FILE_SUFFIX;

public class HandlerClassLoader extends ClassLoader {

    private Path classPath;

    public HandlerClassLoader(Path classPath) {
        this.classPath = classPath;
    }

    /**
     * 获取类文件的位置
     *
     * @param className 全限定类名
     */
    private Path convertClassNameToPath(String className) {
        return classPath.resolve(className.replace('.', '/') + CLASS_FILE_SUFFIX);
    }

    /**
     * 先检查是否存在指定类的 class 文件, 如果不存在, 则由父类加载. 若存在, 则由自身进行加载.
     *
     * @throws ClassNotFoundException
     */
    public Class<?> loadClass(String className) throws ClassNotFoundException {
        Path classFilePath = convertClassNameToPath(className);

        File file = classFilePath.toFile();

        // 不存在的类文件, 转交父加载器进行加载
        if (!file.isFile())
            return super.loadClass(className);

        Class<?> clazz = this.findLoadedClass(className);

        if (clazz != null) return clazz;

        if (file.canRead()) {
            byte[] bytes = loadClassData(className);
            return defineClass(className, bytes, 0, bytes.length);
        }

        throw new ClassNotFoundException(className);
    }


    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 父类已加载
        Class<?> clazz = this.findLoadedClass(name);
        if (clazz == null) {
            byte[] bytes = loadClassData(name);
            clazz = this.defineClass(name, bytes, 0, bytes.length);
        }

        return clazz;
    }

    /**
     * 读取字节码
     */
    private byte[] loadClassData(String className)
            throws ClassNotFoundException {
        Path classFilePath = convertClassNameToPath(className);

        try (FileInputStream in = new FileInputStream(classFilePath.toFile()); ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            int readCount = 0;
            while ((readCount = in.read()) != -1) {
                buffer.write(readCount);
            }
            return buffer.toByteArray();
        } catch (IOException e) {
            throw new ClassNotFoundException(className, e);
        }
    }

}
