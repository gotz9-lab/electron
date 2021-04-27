package com.github.gotz9.electron.protocol;


import com.esotericsoftware.kryo.Kryo;
import com.github.gotz9.electron.protocol.message.ProtocolMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

public class ServerProtocolTester {

    public static class TestUserMessage implements ProtocolMessage {

        private String source;

        private String content;

        private long timestamp;

        public TestUserMessage() {
        }

        public TestUserMessage(String source, String content, long timestamp) {
            this.source = source;
            this.content = content;
            this.timestamp = timestamp;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

    }

    @Test
    public void encodeTest() {
        long timestamp = System.currentTimeMillis();

        // kryo serialize
        TestUserMessage userMessage = new TestUserMessage("test-source", "test-content", timestamp);

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new ElectronServerProtocolInitializer());

        embeddedChannel.writeOutbound(userMessage);

        ByteBuf frame = Unpooled.buffer();

        Object out;
        while ((out = embeddedChannel.readOutbound()) != null) {
            Assert.assertTrue(out instanceof ByteBuf);
            frame.writeBytes((ByteBuf) out);
        }

        embeddedChannel.writeInbound(frame);
        ProtocolMessage message = embeddedChannel.readInbound();

        Assert.assertTrue(message instanceof TestUserMessage);
        TestUserMessage msg = (TestUserMessage) message;
        Assert.assertEquals(userMessage.getSource(), msg.getSource());
        Assert.assertEquals(userMessage.getContent(), msg.getContent());
        Assert.assertEquals(timestamp, msg.getTimestamp());
    }

}
