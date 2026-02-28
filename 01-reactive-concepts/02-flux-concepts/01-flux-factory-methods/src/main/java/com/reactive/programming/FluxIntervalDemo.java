package com.reactive.programming;

import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 *  - We might want to do something every 1 second, or emit 1 message every 500ms.
 *  - This is where interval() comes into picture.
 */
public class FluxIntervalDemo {

    public static void main(String[] args) {

        //Flux.interval(Duration.ofMillis(500)).subscribe(DefaultSubscriber.create("sub1"));

        // This will generate random first names every 500ms
        Flux.interval(Duration.ofMillis(500))
                .map(i -> Util.faker().name().firstName())
                .subscribe(DefaultSubscriber.create("sub1"));

        Util.sleepSeconds(5);
    }
}
