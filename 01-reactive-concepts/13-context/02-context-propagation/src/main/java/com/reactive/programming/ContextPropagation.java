package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

public class ContextPropagation {

    private static final Logger log = LoggerFactory.getLogger(ContextPropagation.class);

    public static void main(String[] args) {

        getWelcomeMessage()
                .concatWith(Flux.merge(producer1(), producer2()))
                .contextWrite(Context.of("user", "John"))
                //.subscribe(DefaultSubscriber.create("sub1"))
        ;

        // If we don't want to propagate context to producer2 then we can pass empty context to producer2
        getWelcomeMessage()
                .concatWith(Flux.merge(producer1(), producer2().contextWrite(ctx -> Context.empty())))
                .contextWrite(Context.of("user", "John"))
                .subscribe(DefaultSubscriber.create("sub2"));

        Util.sleepSeconds(2);
    }

    private static Mono<String> getWelcomeMessage() {
        return Mono.deferContextual(ctx -> {
            //log.info("{}", ctx);
            if(ctx.hasKey("user")) {
                return Mono.just("Welcome %s".formatted(ctx.<String>get("user")));
            }

            return Mono.error(new RuntimeException("Unauthenticated!!"));
        });
    }

    private static Mono<String> producer1() {
        return Mono.<String>deferContextual(ctx -> {
                    log.info("Producer1: {}", ctx);
                    return Mono.empty();
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    private static Mono<String> producer2() {
        return Mono.<String>deferContextual(ctx -> {
                    log.info("Producer2: {}", ctx);
                    return Mono.empty();
                })
                .subscribeOn(Schedulers.parallel());
    }
}
