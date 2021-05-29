package com.github.gotz9.electron.core.test;

import com.github.gotz9.electron.core.compile.ElectronCompiler;
import com.github.gotz9.electron.core.handler.IHandlerManager;
import com.github.gotz9.electron.message.ClientMessage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class ElectronCompilerTest {

    @Test
    public void compileAndLoadTest() throws Exception {
        TemporaryFolder tempFolder = TemporaryFolder.builder().build();
        tempFolder.create();

        File output = tempFolder.getRoot();
        ElectronCompiler compiler = new ElectronCompiler(Paths.get("./target/test-resources"), output.toPath());

        compiler.compileAll();

        IHandlerManager manager = new IHandlerManager(output.toPath());

        try {
            manager.loadHandler();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(manager.getHandler(ClientMessage.ClientMessageType.Login_VALUE));

        tempFolder.delete();
    }

}
