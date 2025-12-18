package com.reactive.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 *  - Defer is a factory method that defers the creation of the Flux until the subscriber subscribes to it.
 */
public class FluxDeferDemo {

    private static final Logger log = LoggerFactory.getLogger(FluxDeferDemo.class);

    public static void main(String[] args) {

        Flux.fromIterable(createList());

        log.info("------------------------------------------");

        log.info("Flux creation deferred");
        Flux.defer(() -> Flux.fromIterable(createList()));
                //.subscribe(DefaultSubscriber.create("sub1"));
    }

    private static List<String> createList() {
        log.info("Creating list");
        Util.sleepSeconds(1);
        return List.of("a", "b", "c", "d", "e", "f");
    }
}
