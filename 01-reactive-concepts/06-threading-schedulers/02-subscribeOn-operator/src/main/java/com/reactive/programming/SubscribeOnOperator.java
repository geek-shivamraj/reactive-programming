package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class SubscribeOnOperator {

    private static final Logger log = LoggerFactory.getLogger(SubscribeOnOperator.class);

    public static void main(String[] args) {

        var flux = Flux.create(sink -> {
                    for (int i = 0; i < 2; i++) {
                        log.info("Generating {}", i);
                        sink.next(i);
                    }
                    sink.complete();
                }).doOnNext(i -> log.info("Value {}", i)).doFirst(() -> log.info("first1"))
                .subscribeOn(Schedulers.boundedElastic())
                .doFirst(() -> log.info("first2"));

        //flux.subscribe(DefaultSubscriber.create("sub1"));

        Runnable runnable1 = () -> flux.subscribe(DefaultSubscriber.create("sub1"));
        Runnable runnable2 = () -> flux.subscribe(DefaultSubscriber.create("sub2"));

        Thread.ofPlatform().start(runnable1);
        Thread.ofPlatform().start(runnable2);

        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(2);
    }
}
