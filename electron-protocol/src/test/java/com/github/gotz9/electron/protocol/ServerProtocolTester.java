package com.github.gotz9.electron.protocol;

import com.github.gotz9.electron.protocol.message.ClientMessage;
import com.github.gotz9.electron.protocol.message.Login;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

public class ServerProtocolTester {

    @Test
    public void encodeTest() {
        long timestamp = System.currentTimeMillis();

        ClientMessage token = ClientMessage.newBuilder()
                .setType(ClientMessage.ClientMessageType.Login)
                .setLogin(Login.newBuilder().setUuid(11004).setToken("token").build())
                .build();

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new ElectronServerProtocolInitializer());

        embeddedChannel.writeOutbound(token);

        ByteBuf frame = Unpooled.buffer();

        Object out;
        while ((out = embeddedChannel.readOutbound()) != null) {
            Assert.assertTrue(out instanceof ByteBuf);
            frame.writeBytes((ByteBuf) out);
        }

        embeddedChannel.writeInbound(frame);
        Object message = embeddedChannel.readInbound();

        Assert.assertTrue(message instanceof ClientMessage);
        ClientMessage msg = (ClientMessage) message;
        Assert.assertEquals(msg.getType(), ClientMessage.ClientMessageType.Login);
        Assert.assertEquals(msg.getLogin().getUuid(), 11004);
        Assert.assertEquals(msg.getLogin().getToken(), "token");
    }

}
