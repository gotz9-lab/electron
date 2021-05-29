package com.github.gotz9.electron.handler.client;

import com.github.gotz9.electron.core.handler.IHandler;
import com.github.gotz9.electron.core.handler.server.ServerMessageHandler;
import com.github.gotz9.electron.message.Notify;
import com.github.gotz9.electron.message.ServerMessage;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@IHandler.MessageHandler(ServerMessage.ServerMessageType.Notify_VALUE)
public class NotifyHandler extends ServerMessageHandler<Notify> {

    private static final Logger LOG = LoggerFactory.getLogger(NotifyHandler.class);

    protected Notify extractBody(ServerMessage message) {
        return message.getNotify();
    }

    @Override
    protected void handleMessage(Channel ctx, Notify message) {
        LOG.info("server notify message: " + message.getMessage());
    }

}
