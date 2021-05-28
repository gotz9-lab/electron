package com.github.gotz9.electron;

import com.github.gotz9.electron.handler.IHandlerManager;
import com.github.gotz9.electron.handler.ManagedHandlersDispatcher;
import com.github.gotz9.electron.protocol.message.ClientMessage;

public class ClientMessageDispatchHandler extends ManagedHandlersDispatcher<ClientMessage> {

    public ClientMessageDispatchHandler(IHandlerManager manager) {
        super(manager);
    }

    @Override
    protected int getHandlerId(ClientMessage msg) {
        return msg.getTypeValue();
    }

}
