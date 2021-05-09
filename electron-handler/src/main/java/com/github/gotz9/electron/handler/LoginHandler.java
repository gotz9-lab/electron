package com.github.gotz9.electron.handler;

import com.github.gotz9.electron.IHandler;
import com.github.gotz9.electron.protocol.message.*;
import io.netty.channel.Channel;

@IHandler.MessageHandler(ClientMessage.ClientMessageType.Login_VALUE)
public class LoginHandler implements IHandler<Login> {

    @Override
    public void handle(Channel ctx, Login message) {
        // TODO login logic
        boolean login = false;

        ctx.writeAndFlush(ServerMessage.newBuilder()
                .setType(ServerMessage.ServerMessageType.Notify)
                .setNotify(Notify.newBuilder()
                        .setType(login ? NotificationType.AUTHENTICATE_SUCCESS : NotificationType.AUTHENTICATE_FAILED)
                        .setMessage(login ? "登录成功" : "登录失败")
                        .build())
                .build());
    }

}