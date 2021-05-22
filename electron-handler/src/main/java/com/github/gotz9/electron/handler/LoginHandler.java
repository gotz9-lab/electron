package com.github.gotz9.electron.handler;

import com.github.gotz9.electron.ServiceContextManager;
import com.github.gotz9.electron.handler.server.ClientMessageHandler;
import com.github.gotz9.electron.protocol.message.*;
import com.github.gotz9.electron.service.AuthenticateService;
import io.netty.channel.Channel;

@IHandler.MessageHandler(ClientMessage.ClientMessageType.Login_VALUE)
public class LoginHandler extends ClientMessageHandler<Login> {

    protected Login extractBody(ClientMessage message) {
        return message.getLogin();
    }

    @Override
    protected void handleMessage(Channel ctx, Login message) {
        AuthenticateService authenticateService = ServiceContextManager.CONTEXT.getBean(AuthenticateService.class);
        boolean login = authenticateService.login(message);

        ctx.writeAndFlush(ServerMessage.newBuilder()
                .setType(ServerMessage.ServerMessageType.Notify)
                .setNotify(Notify.newBuilder()
                        .setType(login ? NotificationType.AUTHENTICATE_SUCCESS : NotificationType.AUTHENTICATE_FAILED)
                        .setMessage(login ? "登录成功" : "登录失败")
                        .build())
                .build());
    }

}
