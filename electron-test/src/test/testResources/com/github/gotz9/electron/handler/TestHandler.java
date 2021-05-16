package com.github.gotz9.electron.handler;

import com.github.gotz9.electron.ServiceContextManager;
import com.github.gotz9.electron.protocol.message.*;
import com.github.gotz9.electron.service.AuthenticateService;
import io.netty.channel.Channel;

@IHandler.MessageHandler(ClientMessage.ClientMessageType.Login_VALUE)
public class TestHandler implements IHandler<Login> {

    @Override
    public void handle(Channel ctx, Login message) {
        
    }

}
