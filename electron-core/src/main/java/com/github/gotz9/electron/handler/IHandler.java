package com.github.gotz9.electron.handler;

import io.netty.channel.Channel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface IHandler<M> {

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MessageHandler {

        int value();

        String description() default "";
    }

    void handle(Channel ctx, M message);

}
