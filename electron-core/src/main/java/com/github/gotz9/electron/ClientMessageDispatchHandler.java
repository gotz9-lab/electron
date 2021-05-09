package com.github.gotz9.electron;

import com.github.gotz9.electron.protocol.message.ClientMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class ClientMessageDispatchHandler extends SimpleChannelInboundHandler<ClientMessage> {

    private final IHandlerManager handlerManager;

    public ClientMessageDispatchHandler(IHandlerManager manager) {
        this.handlerManager = manager;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ClientMessage msg) throws Exception {
        IHandler handler = handlerManager.getHandler(msg.getType().getNumber());

        if (handler == null)
            // 无法处理的消息
            return;

        try {
            handler.handle(ctx.channel(), msg.getLogin());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
