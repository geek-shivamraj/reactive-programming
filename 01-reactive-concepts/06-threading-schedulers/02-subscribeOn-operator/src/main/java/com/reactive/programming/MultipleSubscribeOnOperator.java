package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 *  VVImp. Notes:
 *      - The subscribeOn() operator closest to the producer wins/takes precedence over any other previous subscribeOn() operators
 *          & the closest subscribeOn() scheduler will take effect upstream beyond that point.
 *
 *      - For existing inbuilt Publisher library, the lib developer by default adds subscribeOn() operator to the publisher to control
 *          threading or scheduler on which the task will execute efficiently.
 *      - So, if we're using the inbuilt library & try to add our subscribeOn() operator, it will not take any effect.
 *
 *      - It's also recommended if in case we're building/exposing a Publisher/Producer, we should add subscribeOn() operator to it to
 *          control threading or scheduler on which the task should execute efficiently.
 *
 */
public class MultipleSubscribeOnOperator {

    private static final Logger log = LoggerFactory.getLogger(MultipleSubscribeOnOperator.class);

    public static void main(String[] args) {

        var flux = Flux.create(sink -> {
                    for (int i = 0; i < 2; i++) {
                        log.info("Generating {}", i);
                        sink.next(i);
                    }
                    sink.complete();
                })
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
}
