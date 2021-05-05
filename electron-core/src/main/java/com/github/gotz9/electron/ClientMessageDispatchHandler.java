package com.github.gotz9.electron;

import com.github.gotz9.electron.core.service.AuthenticateService;
import com.github.gotz9.electron.protocol.message.ClientMessage;
import com.github.gotz9.electron.protocol.message.NotificationType;
import com.github.gotz9.electron.protocol.message.Notify;
import com.github.gotz9.electron.protocol.message.ServerMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class ClientMessageDispatchHandler extends SimpleChannelInboundHandler<ClientMessage> {

    private final AuthenticateService authenticateService;

    public ClientMessageDispatchHandler(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ClientMessage msg) throws Exception {
        switch (msg.getType()) {
            case Login: {
                boolean login = authenticateService.login(msg.getLogin());
                ctx.writeAndFlush(ServerMessage.newBuilder()
                        .setType(ServerMessage.ServerMessageType.Notify)
                        .setNotify(Notify.newBuilder()
                                .setType(login ? NotificationType.AUTHENTICATE_SUCCESS : NotificationType.AUTHENTICATE_FAILED)
                                .setMessage(login ? "登录成功" : "登录失败")
                                .build())
                        .build());
            }
            break;
            case UNRECOGNIZED:
            default:
                // TODO log
        }
    }

}
