package com.github.gotz9.electron.client;

import com.github.gotz9.electron.core.compile.ElectronCompiler;
import com.github.gotz9.electron.core.handler.IHandlerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class ClientApp {

    private static final Logger LOG = LoggerFactory.getLogger(ClientApp.class);

    private static final String DEFAULT_HANDLER_BIN_PATH = "./target/test-client-bin";

    private static final String DEFAULT_HANDLER_SOURCE_PATH = "./electron-handler/electron-handler-client/src/main/java";

    public static void main(String[] args) {
        try {
            ElectronCompiler compiler = new ElectronCompiler(DEFAULT_HANDLER_SOURCE_PATH, DEFAULT_HANDLER_BIN_PATH);
            compiler.compileAll();
        } catch (Exception e) {
            LOG.error("handler compile failed", e);
            return;
        }

        IHandlerManager iHandlerManager = new IHandlerManager(DEFAULT_HANDLER_BIN_PATH);
        try {
            iHandlerManager.loadHandler();
        } catch (Exception e) {
            LOG.error("handler manager failed", e);
            return;
        }

        final ElectronClient client = new ElectronClient(new InetSocketAddress("localhost", 8080), iHandlerManager);

        client.login();

        client.close();
    }

}
