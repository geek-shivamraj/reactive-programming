package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 *  - It's also recommended if in case we're building/exposing a Publisher/Producer, we should add subscribeOn() operator to it to
 *      control threading or scheduler on which the task should execute efficiently.
 *
 *  - Here we can see, lib-parallel thread pool is taking precedence over custom-parallel thread pool / boundedElastic thread pool.
 *
 */
public class ExternalFluxLibDemo {

    private static final Logger log = LoggerFactory.getLogger(ExternalFluxLibDemo.class);

    public static void main(String[] args) {

        var flux = existingFluxLibrary()
                .subscribeOn(Schedulers.newParallel("custom-parallel"))   // subscribeOn 2: custom-parallel thread pool
                .doOnNext(i -> log.info("Value {}", i))
                .doFirst(() -> log.info("first1"))
                .subscribeOn(Schedulers.boundedElastic())   // subscribeOn 1: boundedElastic thread pool
                .doFirst(() -> log.info("first2"));

        Runnable runnable = () -> flux.subscribe(DefaultSubscriber.create("sub1"));
        Thread.ofPlatform().start(runnable);

        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(2);
    }

    private static Flux<Integer> existingFluxLibrary() {
        return Flux.create(sink -> {
                    for (int i = 0; i < 2; i++) {
                        log.info("Generating {}", i);
                        sink.next(i);
                    }
                    sink.complete();
                })
                .subscribeOn(Schedulers.newParallel("lib-parallel"))    // subscribeOn 3: lib-parallel thread pool
                .cast(Integer.class);
    }
}
