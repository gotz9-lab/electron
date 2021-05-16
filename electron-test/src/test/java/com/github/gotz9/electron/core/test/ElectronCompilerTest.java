package com.github.gotz9.electron.core.test;

import com.github.gotz9.electron.compile.ElectronCompiler;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runners.JUnit4;

import java.io.File;
import java.nio.file.Paths;

public class ElectronCompilerTest {

    @Test
    public void compileTest() throws Exception {
        TemporaryFolder tempFolder = TemporaryFolder.builder().build();
        tempFolder.create();

        File output = tempFolder.getRoot();
        ElectronCompiler compiler = new ElectronCompiler(Paths.get("./target/test-classes"), output.toPath());

        compiler.compile("com.github.gotz9.electron.compile.CompileTestSource");

        tempFolder.delete();
    }

}
