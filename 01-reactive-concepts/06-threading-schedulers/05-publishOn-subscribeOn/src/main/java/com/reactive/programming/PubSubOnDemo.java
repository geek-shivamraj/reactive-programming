package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class PubSubOnDemo {

    private static final Logger log = LoggerFactory.getLogger(PubSubOnDemo.class);
    public static void main(String[] args) {

        var flux = Flux.create(sink -> {
                    for (int i = 0; i < 2; i++) {
                        log.info("Generating {}", i);
                        sink.next(i);
                    }
                    sink.complete();
                })
                .publishOn(Schedulers.parallel())
                .doOnNext(i -> log.info("Value {}", i))
                .doFirst(() -> log.info("first1"))
                .subscribeOn(Schedulers.boundedElastic())         // Switches the thread to boundedElastic for downstream
                .doFirst(() -> log.info("first2"));

        Runnable runnable = () -> flux.subscribe(DefaultSubscriber.create("sub1"));
        Thread.ofPlatform().start(runnable);

        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(2);
    }
}
