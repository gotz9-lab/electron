package com.github.gotz9.electron.protocol.codec;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.ByteBufferInputStream;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.gotz9.electron.protocol.message.ProtocolMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

public class KryoCodecHandler extends MessageToMessageCodec<ByteBuf, ProtocolMessage> {

    public final Kryo kryo = new Kryo();

    {
        kryo.register(ProtocolMessage.class);
    }

    public KryoCodecHandler() {
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ProtocolMessage msg, List<Object> out) throws Exception {
        Output output = new Output(256, -1);
        kryo.writeClassAndObject(output, msg);
        out.add(Unpooled.wrappedBuffer(output.toBytes()));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Output output = new Output(in.readableBytes(), -1);
        in.readBytes(output, in.readableBytes());

        Object o = kryo.readClassAndObject(new Input(output.getBuffer()));

        if (o instanceof ProtocolMessage) {
            out.add(o);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    public static class ByteBufInput extends Input {

        private final ByteBuf byteBuf;

        ByteBufInput(ByteBuf byteBuf) {
            this.byteBuf = byteBuf;
        }

        @Override
        public int read() throws KryoException {
            return byteBuf.readableBytes() > 0 ? byteBuf.readByte() : -1;
        }

    }

    public static class ByteBufOutput extends Output {

        private final ByteBuf byteBuf;

        public ByteBufOutput() {
            this(Unpooled.buffer());
        }

        public ByteBufOutput(ByteBuf byteBuf) {
            this.byteBuf = byteBuf;
        }

    }

}



