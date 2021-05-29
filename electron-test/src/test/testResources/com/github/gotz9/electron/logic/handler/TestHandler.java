package com.github.gotz9.electron.logic.handler;

import com.github.gotz9.electron.core.handler.IHandler;
import com.github.gotz9.electron.message.*;
import com.github.gotz9.electron.core.handler.server.ClientMessageHandler;
import io.netty.channel.Channel;

@IHandler.MessageHandler(ClientMessage.ClientMessageType.Login_VALUE)
public class TestHandler extends ClientMessageHandler<Login> {

    @Override
    protected Login extractBody(ClientMessage message) {
        return message.getLogin();
    }

    @Override
    protected void handleMessage(Channel ctx, Login body) {

    }

}
