package com.github.gotz9.electron.protocol;

import com.github.gotz9.electron.protocol.message.ClientMessage;
import com.github.gotz9.electron.protocol.message.Login;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

public class ServerProtocolTest {

    @Test
    public void encodeTest() {
        ClientMessage token = ClientMessage.newBuilder()
                .setType(ClientMessage.ClientMessageType.Login)
                .setLogin(Login.newBuilder().setUuid(11004).setToken("token").build())
                .build();

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new ElectronProtocolInitializer(ClientMessage.getDefaultInstance()) {
            @Override
            protected ChannelHandler getDispatcher() {
                return new SimpleChannelInboundHandler<ClientMessage>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ClientMessage msg) throws Exception {
                        Assert.assertEquals(msg.getType(), ClientMessage.ClientMessageType.Login);
                        Assert.assertEquals(msg.getLogin().getUuid(), 11004);
                        Assert.assertEquals(msg.getLogin().getToken(), "token");
                    }
                };
            }
        });

        embeddedChannel.writeOutbound(token);

        ByteBuf frame = Unpooled.buffer();

        Object out;
        while ((out = embeddedChannel.readOutbound()) != null) {
            Assert.assertTrue(out instanceof ByteBuf);
            frame.writeBytes((ByteBuf) out);
        }

        embeddedChannel.writeInbound(frame);
    }

}
