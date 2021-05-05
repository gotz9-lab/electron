package com.github.gotz9.electron;

import com.github.gotz9.electron.core.service.AuthenticateService;
import com.github.gotz9.electron.protocol.ElectronServerProtocolInitializer;
import com.github.gotz9.electron.protocol.message.ClientMessage;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerChannelInitializer extends ElectronServerProtocolInitializer {

    private final ClientMessageDispatchHandler dispatchHandler;

    public ServerChannelInitializer(AuthenticateService authenticateService) {
        super(ClientMessage.getDefaultInstance());
        this.dispatchHandler = new ClientMessageDispatchHandler(authenticateService);
    }

    @Override
    protected SimpleChannelInboundHandler<ClientMessage> getDispatcher() {
        return dispatchHandler;
    }

}
