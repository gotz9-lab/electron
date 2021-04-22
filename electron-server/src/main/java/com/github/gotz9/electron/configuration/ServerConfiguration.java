package com.github.gotz9.electron.configuration;
public class ServerConfiguration {

    private final short port;

    private final int actor;

    private final int worker;

    public ServerConfiguration(short port, int actor, int worker) {
        this.port = port;
        this.actor = actor;
        this.worker = worker;
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

}
