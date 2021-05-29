package com.github.gotz9.electron.client;

import com.github.gotz9.electron.handler.IHandlerManager;
import com.github.gotz9.electron.protocol.ElectronProtocolInitializer;
import com.github.gotz9.electron.protocol.message.ServerMessage;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientChannelInitializer extends ElectronProtocolInitializer {

    private final ServerMessageDispatchHandler dispatchHandler;

    public ClientChannelInitializer(IHandlerManager manager) {
        super(ServerMessage.getDefaultInstance());
        this.dispatchHandler = new ServerMessageDispatchHandler(manager);
    }

    @Override
    protected SimpleChannelInboundHandler<ServerMessage> getDispatcher() {
        return dispatchHandler;
    }

}
