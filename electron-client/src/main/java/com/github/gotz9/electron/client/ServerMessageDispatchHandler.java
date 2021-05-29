package com.github.gotz9.electron.client;

import com.github.gotz9.electron.core.handler.IHandlerManager;
import com.github.gotz9.electron.core.handler.ManagedHandlersDispatcher;
import com.github.gotz9.electron.message.ServerMessage;

public class ServerMessageDispatchHandler extends ManagedHandlersDispatcher<ServerMessage> {

    public ServerMessageDispatchHandler(IHandlerManager manager) {
        super(manager);
    }

    @Override
    protected int getHandlerId(ServerMessage msg) {
        return msg.getTypeValue();
    }

}
