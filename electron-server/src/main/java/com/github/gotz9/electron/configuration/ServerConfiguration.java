package com.github.gotz9.electron.configuration;

public class ServerConfiguration {

    public static final String DEFAULT_HANDLER_BIN_PATH = "./handlerBin";

    private final short port;

    private final int actor;

    private final int worker;

    private final String handlerBinPath;

    private final Class<?>[] configurations;

    public ServerConfiguration(short port, int actor, int worker, String handlerBinPath, Class<?>[] configurations) {
        this.port = port;
        this.actor = actor;
        this.worker = worker;
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

    public String getHandlerBinPath() {
        return handlerBinPath;
    }

    public Class<?>[] getRegisteredConfiguration() {
        return configurations;
    }

}
