package com.github.gotz9.electron.server.configuration;

public class ServerConfiguration {

    public static final String DEFAULT_HANDLER_BIN_PATH = "./handlerBin";

    public static final String DEFAULT_HANDLER_SRC_PATH = "./handlerSrc";

    private final short port;

    private final int actor;

    private final int worker;

    private final String handlerBinPath;

    private final String handlerSrcPath;

    private final Class<?>[] configurations;

    public ServerConfiguration(short port, int actor, int worker, String handlerSrcPath, String handlerBinPath, Class<?>[] configurations) {
        this.port = port;
        this.actor = actor;
        this.worker = worker;
        this.handlerSrcPath = handlerSrcPath;
        this.handlerBinPath = handlerBinPath;
        this.configurations = configurations;
    }

    public short getPort() {
        return port;
    }

    public int getActor() {
        return actor;
    }

    public int getWorker() {
        return worker;
    }

    public String getHandlerSrcPath() {
        return handlerSrcPath;
    }

    public String getHandlerBinPath() {
        return handlerBinPath;
    }

    public Class<?>[] getRegisteredConfiguration() {
        return configurations;
    }

}
