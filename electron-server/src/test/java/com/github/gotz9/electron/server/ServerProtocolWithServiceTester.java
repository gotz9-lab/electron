package com.github.gotz9.electron.server;

import com.github.gotz9.electron.ClientChannelInitializer;
import com.github.gotz9.electron.Server;
import com.github.gotz9.electron.ServerChannelInitializer;
import com.github.gotz9.electron.core.service.impl.DefaultAuthenticateService;
import com.github.gotz9.electron.protocol.ElectronServerProtocolInitializer;
import com.github.gotz9.electron.protocol.message.ClientMessage;
import com.github.gotz9.electron.protocol.message.Login;
import com.github.gotz9.electron.protocol.message.NotificationType;
import com.github.gotz9.electron.protocol.message.ServerMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

public class ServerProtocolWithServiceTester {

    @Test
    public void encodeTest() {
        ClientMessage token = ClientMessage.newBuilder()
                .setType(ClientMessage.ClientMessageType.Login)
                .setLogin(Login.newBuilder().setUuid(11004).setToken("token").build())
                .build();

        EmbeddedChannel serverChannel = new EmbeddedChannel(new ServerChannelInitializer(new DefaultAuthenticateService()));
        EmbeddedChannel clientChannel = new EmbeddedChannel(new ClientChannelInitializer() {
            @Override
            protected ChannelHandler getDispatcher() {
                return new SimpleChannelInboundHandler<ServerMessage>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ServerMessage msg) throws Exception {
                        ctx.fireChannelRead(msg);
                    }
                };
            }
        });

        clientChannel.writeOutbound(token);

        ByteBuf frame = Unpooled.buffer();

        Object out;
        while ((out = clientChannel.readOutbound()) != null) {
            Assert.assertTrue(out instanceof ByteBuf);
            frame.writeBytes((ByteBuf) out);
        }

        serverChannel.writeInbound(frame);

        frame = Unpooled.buffer();
        while ((out = serverChannel.readOutbound()) != null) {
            Assert.assertTrue(out instanceof ByteBuf);
            frame.writeBytes((ByteBuf) out);
        }
        clientChannel.writeInbound(frame);

        Object in = clientChannel.readInbound();

        Assert.assertTrue(in instanceof ServerMessage);
        ServerMessage msg = (ServerMessage) in;
        Assert.assertEquals(msg.getType(), ServerMessage.ServerMessageType.Notify);
        Assert.assertEquals(msg.getNotify().getType(), NotificationType.AUTHENTICATE_FAILED);
        Assert.assertEquals(msg.getNotify().getMessage(), "登录失败");
    }

}
