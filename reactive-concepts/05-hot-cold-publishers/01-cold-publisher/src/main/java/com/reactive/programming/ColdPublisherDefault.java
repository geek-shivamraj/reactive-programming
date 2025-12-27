package com.reactive.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class ColdPublisherDefault {

    private static final Logger log = LoggerFactory.getLogger(ColdPublisherDefault.class);

    public static void main(String[] args) {

        var flux = Flux.create(fluxSink -> {
            log.info("Invoked!!");
            for (int i = 0; i < 3; i++) {
                fluxSink.next(i);
            }
            fluxSink.complete();
        });

        flux.subscribe(DefaultSubscriber.create("sub1"));
        flux.subscribe(DefaultSubscriber.create("sub2"));
    }
}
