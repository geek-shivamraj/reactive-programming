package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/**
 *  - Context is similar feature as ThreadLocal however unlike ThreadLocal, the context is very safe to use
 *      & it's immutable.
 *  - Context is for providing metadata about the request (similar to HTTP headers)
 *  - A subscriber can pass context info/write using contextWrite() method.
 *
 */
public class ContextIntro {

    private static final Logger log = LoggerFactory.getLogger(ContextIntro.class);

    public static void main(String[] args) {

        getWelcomeMessage()
                .contextWrite(Context.of("user", "John"))
                .contextWrite(Context.of("a", "b"))
                .subscribe(DefaultSubscriber.create("sub1"));
    }

    private static Mono<String> getWelcomeMessage() {
        return Mono.deferContextual(ctx -> {
           log.info("{}", ctx);
           if(ctx.hasKey("user")) {
               return Mono.just("Welcome %s".formatted(ctx.<String>get("user")));
           }

           return Mono.error(new RuntimeException("Unauthenticated!!"));
        });
    }

}
