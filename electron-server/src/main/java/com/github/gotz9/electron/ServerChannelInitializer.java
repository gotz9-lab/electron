package com.github.gotz9.electron;

import com.github.gotz9.electron.handler.IHandlerManager;
import com.github.gotz9.electron.protocol.ElectronProtocolInitializer;
import com.github.gotz9.electron.protocol.message.ClientMessage;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerChannelInitializer extends ElectronProtocolInitializer {

    private final ClientMessageDispatchHandler dispatchHandler;

    public ServerChannelInitializer(IHandlerManager manager) {
        super(ClientMessage.getDefaultInstance());
        this.dispatchHandler = new ClientMessageDispatchHandler(manager);
    }

    @Override
    protected SimpleChannelInboundHandler<ClientMessage> getDispatcher() {
        return dispatchHandler;
    }

}
