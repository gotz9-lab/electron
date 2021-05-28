package com.github.gotz9.electron.client;

import com.github.gotz9.electron.handler.IHandlerManager;
import com.github.gotz9.electron.handler.ManagedHandlersDispatcher;
import com.github.gotz9.electron.protocol.message.ServerMessage;

public class ServerMessageDispatchHandler extends ManagedHandlersDispatcher<ServerMessage> {

    public ServerMessageDispatchHandler(IHandlerManager manager) {
        super(manager);
    }

    @Override
    protected int getHandlerId(ServerMessage msg) {
        return msg.getTypeValue();
    }

}
