package com.reactive.programming.dos.callbacks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

/**
 *  - Instead of using .subscribe(DefaultSubscriber.create()), we can use doOnNext() operator to log & use just .subscribe()
 */
public class SubscribeDoNextDemo {

    private static final Logger log = LoggerFactory.getLogger(SubscribeDoNextDemo.class);

    public static void main(String[] args) {
        Flux.range(1, 10)
                .doOnNext(i -> log.info("Received: {}", i))
                .doOnComplete(() -> log.info("Completed"))
                .doOnError(e -> log.error("Error", e))
                .subscribe();
    }
}
