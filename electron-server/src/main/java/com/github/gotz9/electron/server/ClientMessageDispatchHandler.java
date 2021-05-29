package com.github.gotz9.electron.server;

import com.github.gotz9.electron.core.handler.IHandlerManager;
import com.github.gotz9.electron.core.handler.ManagedHandlersDispatcher;
import com.github.gotz9.electron.message.ClientMessage;

public class ClientMessageDispatchHandler extends ManagedHandlersDispatcher<ClientMessage> {

    public ClientMessageDispatchHandler(IHandlerManager manager) {
        super(manager);
    }

    @Override
    protected int getHandlerId(ClientMessage msg) {
        return msg.getTypeValue();
    }

}
