package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

public class ContextAppendAndUpdate {

    private static final Logger log = LoggerFactory.getLogger(ContextAppendAndUpdate.class);

    public static void main(String[] args) {

        //append();

        update();
    }

    private static void append() {

        getWelcomeMessage()
                .contextWrite(Context.of("user", "John").put("c", "d").put("e", "f"))
                .contextWrite(Context.of("a", "b"))
                .subscribe(DefaultSubscriber.create("sub1"));
    }

    private static void update() {

        getWelcomeMessage()
                .contextWrite(ctx -> ctx.put("user", ctx.get("user").toString().toUpperCase()))
                .contextWrite(ctx -> ctx.delete("c"))    // Delete a key
                //.contextWrite(ctx -> Context.of("user", "mike"))    // override the existing key "user"
                //.contextWrite(ctx -> Context.empty())               // clear the context (from bottom to top)
                .contextWrite(Context.of("user", "John").put("c", "d").put("e", "f"))
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
