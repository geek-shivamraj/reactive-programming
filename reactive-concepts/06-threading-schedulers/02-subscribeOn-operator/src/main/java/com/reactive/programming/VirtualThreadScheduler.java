package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 *  - Project Reactor supports virtual threads if we like to use.
 *      - We already know that "parallel" thread pool/scheduler is for CPU tasks. Virtual Threads are not for CPU tasks & hence we can't use it for CPU tasks.
 *      - Virtual threads are for time-consuming IO network tasks for blocking operations so "boundedElastic" thread pool/scheduler can use virtual threads.
 *      - We've to set System.setProperty(reactor.schedulers.defaultBoundedElasticOnVirtualThreads, true) system property to enable virtual threads.
 */
public class VirtualThreadScheduler {

    private static final Logger log = LoggerFactory.getLogger(VirtualThreadScheduler.class);

    public static void main(String[] args) {

        // Explicitly enable virtual threads on boundedElastic scheduler
        System.setProperty("reactor.schedulers.defaultBoundedElasticOnVirtualThreads", "true");

        var flux = Flux.create(sink -> {
                    for (int i = 0; i < 2; i++) {
                        log.info("Generating {}", i);
                        sink.next(i);
                    }
                    sink.complete();
                })
                .doOnNext(i -> log.info("Value {}", i))
                .doFirst(() -> log.info("first1 - {}", Thread.currentThread().isVirtual()))
                .subscribeOn(Schedulers.boundedElastic())
                .doFirst(() -> log.info("first2"));

        Runnable runnable = () -> flux.subscribe(DefaultSubscriber.create("sub1"));
        Thread.ofPlatform().start(runnable);

        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(2);
    }
}
