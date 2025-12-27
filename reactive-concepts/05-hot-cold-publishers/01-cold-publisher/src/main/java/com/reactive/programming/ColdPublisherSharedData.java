package com.reactive.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *  - If we try to manage state outside the Flux.create(), then state will be shared among the subscribers.
 */
public class ColdPublisherSharedData {

    public static final Logger log = LoggerFactory.getLogger(ColdPublisherSharedData.class);
    public static void main(String[] args) {

        // Below atomic integer is shared among the subscribers & it's not scoped per subscription.
        AtomicInteger atomicInteger = new AtomicInteger(0);
        var flux = Flux.create(fluxSink -> {
            log.info("Invoked!!");
            for (int i = 0; i < 3; i++) {
                fluxSink.next(atomicInteger.getAndIncrement());
            }
            fluxSink.complete();
        });

        flux.subscribe(DefaultSubscriber.create("sub1"));
        flux.subscribe(DefaultSubscriber.create("sub2"));
        flux.subscribe(DefaultSubscriber.create("sub3"));
    }
}
