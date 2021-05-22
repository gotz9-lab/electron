package com.github.gotz9.electron;

import com.github.gotz9.electron.handler.IHandler;
import com.github.gotz9.electron.handler.IHandlerManager;
import com.github.gotz9.electron.protocol.message.ClientMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class ClientMessageDispatchHandler extends SimpleChannelInboundHandler<ClientMessage> {

    private static final Logger logger = LoggerFactory.getLogger(ClientMessageDispatchHandler.class);

    private final IHandlerManager handlerManager;

    public ClientMessageDispatchHandler(IHandlerManager manager) {
        this.handlerManager = manager;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ClientMessage msg) throws Exception {
        IHandler handler = handlerManager.getHandler(msg.getType().getNumber());

        if (handler == null) {
            // 无法处理的消息
            logger.warn("unhandlable message: not found handler for {}", msg.getType());
            return;
        }

        try {
            handler.handle(ctx.channel(), msg);
        } catch (Exception e) {
            logger.error("handler exception", e);
        }
    }

}
