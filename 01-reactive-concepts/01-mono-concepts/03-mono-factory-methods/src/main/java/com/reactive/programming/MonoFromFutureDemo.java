package com.reactive.programming;

import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

/**
 *  - Mono.fromFuture adapts a CompletableFuture into a Mono. It emits the future’s result once available, propagates errors if the future fails,
 *      and completes afterward.
 *  - Perfect for bridging Java’s async futures into Reactor’s reactive streams.
 */
public class MonoFromFutureDemo {

    private static final Logger log = LoggerFactory.getLogger(MonoFromFutureDemo.class);

    // We need to explicitly block the main thread to see the value.
    public static void main(String[] args) {

        //By default, the CompletableFuture is not lazy i.e., if we create the CompletableFuture object, it will execute almost immediately.
        Mono.fromFuture(getName()).subscribe(DefaultSubscriber.create("sub1"));
        Util.sleepSeconds(1);

        log.info("-----------------------------------------------------------");

        // To make it lazy, we can use other overloaded fromFuture via Supplier
        Mono.fromFuture(() -> getName());
               // .subscribe(DefaultSubscriber.create("sub2"));


    }

    /**
     *  - We will see "Generating Name" but we will not receive value becoz CompletableFuture will use a
     *      separate ThreadPool (so that's what it actually prints)
     *  - By default, the CompletableFuture is not lazy i.e., if we create the CompletableFuture object, it will execute
     *      almost immediately.
     */
    private static CompletableFuture<String> getName() {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Generating name");
            return Util.faker().name().fullName();
        });
    }
}
