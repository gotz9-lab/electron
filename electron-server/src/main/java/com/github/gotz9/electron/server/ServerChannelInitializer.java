package com.github.gotz9.electron.server;

import com.github.gotz9.electron.core.handler.IHandlerManager;
import com.github.gotz9.electron.protocol.ElectronProtocolInitializer;
import com.github.gotz9.electron.message.ClientMessage;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Objects;
import java.util.concurrent.ExecutorService;

public class ServerChannelInitializer extends ElectronProtocolInitializer {

    private final ClientMessageDispatchHandler dispatchHandler;

    public ServerChannelInitializer(IHandlerManager manager, ExecutorService executor) {
        super(ClientMessage.getDefaultInstance());
        this.dispatchHandler = new ClientMessageDispatchHandler(manager, executor);
    }

    public ServerChannelInitializer(IHandlerManager manager) {
        this(manager, null);
    }

    @Override
    protected SimpleChannelInboundHandler<ClientMessage> getDispatcher() {
        return dispatchHandler;
    }

}
