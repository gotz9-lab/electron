package com.github.gotz9.electron.protocol;

import com.google.protobuf.MessageLite;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

/**
 * 读入 length based frame, 通过协议栈解析最终得到 ProtocolMessage 对象
 */
public abstract class ElectronServerProtocolInitializer extends ChannelInitializer<Channel> {

    private static final UnhandledMessageLogger UNHANDLED_MESSAGE_LOGGER = new UnhandledMessageLogger();

    protected static final String MESSAGE_DISPATCHER = "message-dispatcher";

    private final ProtobufDecoder protobufDecoder;
    private final ProtobufEncoder protobufEncoder = new ProtobufEncoder();

    public ElectronServerProtocolInitializer(MessageLite messageLite) {
        this.protobufDecoder = new ProtobufDecoder(messageLite);
    }

    @Override
    protected void initChannel(Channel ch) {
        // 初始化协议栈
        ch.pipeline()
                .addLast("frame-decode", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, Integer.BYTES, 0, Integer.BYTES))
                .addLast("frame-encode", new LengthFieldPrepender(Integer.BYTES));

        ch.pipeline()
                .addLast("protobuf-encoder", protobufEncoder)
                .addLast("message-decoder", protobufDecoder);

        ChannelHandler dispatcher = getDispatcher();
        if (dispatcher != null)
            ch.pipeline().addLast(MESSAGE_DISPATCHER, dispatcher);
    }

    protected ChannelHandler getDispatcher() {
        return UNHANDLED_MESSAGE_LOGGER;
    }

}