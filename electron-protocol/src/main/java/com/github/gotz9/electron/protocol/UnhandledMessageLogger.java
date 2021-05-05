package com.github.gotz9.electron.protocol;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class UnhandledMessageLogger extends SimpleChannelInboundHandler {

    public static final Logger logger = LoggerFactory.getLogger(UnhandledMessageLogger.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        logger.warn("fire unhandled message: [type={}, object={}]", msg.getClass().getTypeName(), msg);
        ctx.fireChannelRead(msg);
    }

}
