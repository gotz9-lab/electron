package com.github.gotz9.electron.compile;

import javax.tools.*;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.gotz9.electron.utils.ElectronUtils.JAVA_FILE_SUFFIX;

public class ElectronCompiler {

    private final Path source;

    private final Path output;

    public ElectronCompiler(String source, String output) {
        this(Paths.get(source), Paths.get(output));
    }

    public ElectronCompiler(Path source, Path output) {
        this.source = Objects.requireNonNull(source, "source path").normalize();
        this.output = Objects.requireNonNull(output, "output path").normalize();
    }

    /**
     * 编译指定目录下的所有 .java 文件
     *
     * @throws Exception
     */
    public void compileAll() throws Exception {
        Path path = source.toAbsolutePath();

        Set<File> files = new HashSet<>();
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                final File f = file.toFile();
                if (f.canRead() && f.getName().endsWith(JAVA_FILE_SUFFIX)) {
                    files.add(f);
                }
                return FileVisitResult.CONTINUE;
            }
        });

        compile(files);
    }

    /**
     * 编译 source 目录下 className 指定的目录下的文件.
     *
     * @param className 全限定类名, 会根据包名寻找文件源文件位置
     * @throws Exception
     */
    public void compile(String className) throws Exception {
        File sourceFile = source.toAbsolutePath().resolve(className.replace(".", File.separator) + JAVA_FILE_SUFFIX).normalize().toFile();

        if (!sourceFile.exists() || !sourceFile.canRead()) {
            // TODO throw Exception
            return;
        }

        compile(Collections.singleton(sourceFile));
    }

    public void compile(Collection<File> source) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        File outputDir = output.toAbsolutePath().normalize().toFile();
        if (!outputDir.isDirectory()) {
            // TODO throw Exception
            return;
        }

        URLClassLoader classLoader = (URLClassLoader) getClass().getClassLoader();
        StringBuilder classPathBuilder = new StringBuilder();
        for (URL url : classLoader.getURLs()) {
            String p = url.getFile();
            classPathBuilder.append(p).append(File.pathSeparator);
        }

        List<String> options = new LinkedList<>();

        options.add("-encoding");
        options.add("UTF-8");
        options.add("-classpath");
        options.add(classPathBuilder.toString());
        options.add("-d");
        options.add(outputDir.getAbsolutePath());

        System.out.println("output path:" + outputDir.getAbsolutePath());

        // 添加要编译的目标源文件
        Set<JavaFileObject> units = source.stream().map(EJavaFileObject::new).collect(Collectors.toSet());

        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnosticCollector, null, null);

        JavaCompiler.CompilationTask compilerTask = compiler.getTask(null, fileManager, diagnosticCollector, options, null, units);

        Boolean result = compilerTask.call();

        if (!result) {
            String message = diagnosticCollector.getDiagnostics().stream().map(diagnostic -> diagnostic.getMessage(Locale.CHINA)).collect(Collectors.joining("\n"));
            throw new Exception(message);
        }
    }

}
