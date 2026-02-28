package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 *  - Use case:
 *      - There might be some method that might be accepting some scheduler as an argument & we want to execute it
 *          on the current thread then in that case, we can use immediate() scheduler.
 */
public class ImmediateScheduler {

    private static final Logger log = LoggerFactory.getLogger(ImmediateScheduler.class);

    public static void main(String[] args) {

        var flux = Flux.create(sink -> {
                    for (int i = 0; i < 2; i++) {
                        log.info("Generating Value: {}", i);
                        sink.next(i);
                    }
                    sink.complete();
                })
                .subscribeOn(Schedulers.immediate())    // immediate thread pool: current thread will keep on executing
                .doOnNext(i -> log.info("Current Value: {}", i))
                .doFirst(() -> log.info("first1"))
                .subscribeOn(Schedulers.boundedElastic())    // boundedElastic thread pool
                .doFirst(() -> log.info("first2"));

        Runnable runnable = () -> flux.subscribe(DefaultSubscriber.create("sub1"));
        Thread.ofPlatform().start(runnable);

        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(2);
    }

    private static void someMethod(Scheduler scheduler) {

    }
}
