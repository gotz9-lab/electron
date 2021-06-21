package com.github.gotz9.electron.core.compile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.*;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.gotz9.electron.core.utils.ElectronUtils.JAVA_FILE_SUFFIX;

public class ElectronCompiler {

    private static final Logger LOG = LoggerFactory.getLogger(ElectronCompiler.class);

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
     * 默认编译文件筛选条件, 需要文件可读且文件名后缀为 {@link com.github.gotz9.electron.core.utils.ElectronUtils#JAVA_FILE_SUFFIX}.
     */
    private static final Predicate<File> DEFAULT_COMPILE_TARGET_PREDICATE = file -> file.canRead() && file.getName().endsWith(JAVA_FILE_SUFFIX);

    public void compileAll() throws Exception {
        compileAll(DEFAULT_COMPILE_TARGET_PREDICATE);
    }

    /**
     * 编译指定目录下通过 targetPredicate 断言的所有文件
     *
     * @throws Exception
     */
    public void compileAll(Predicate<File> targetPredicate) throws Exception {
        Path path = source.toAbsolutePath();

        Set<File> files = new HashSet<>();
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                final File f = file.toFile();
                if (targetPredicate.test(f)) {
                    LOG.debug("add Java source file: {}", file);
                    files.add(f);
                }
                return FileVisitResult.CONTINUE;
            }
        });

        compile(files);
    }

    /**
     * 编译 source 目录下 fullClassName 指定的目录下的文件.
     *
     * @param fullClassName 全限定类名, 会根据包名寻找文件源文件位置
     * @throws Exception
     */
    public void compile(String fullClassName) throws Exception {
        File sourceFile = source.toAbsolutePath().resolve(fullClassName.replace(".", File.separator) + JAVA_FILE_SUFFIX).normalize().toFile();

        if (!sourceFile.exists() || !sourceFile.canRead()) {
            // TODO throw Exception
            return;
        }

        compile(Collections.singleton(sourceFile));
    }

    /**
     * @param source 需要编译的目标文件集合, 如果 {@link Collection#isEmpty()}, 将跳过编译行为.
     * @throws Exception 编译异常
     */
    public void compile(Collection<File> source) throws Exception {
        if (source.isEmpty()) {
            LOG.info("skip compiling with empty source file set.");
            return;
        }

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

        LOG.info("output path:" + outputDir.getAbsolutePath());

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
