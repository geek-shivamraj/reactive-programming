package com.reactive.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

/**
 *  - Log operator is used to log the events in the reactive stream.
 *  - Operators are functions you chain on a Flux or Mono to transform, filter, or observe the stream.
 *  - log() is a side effect operator: it doesn’t change the data, but it intercepts the reactive signals
 *      (onSubscribe, onNext, onError, onComplete) and prints them to the console.
 *
 *  - In Reactive Streams, every operator (including log()) is implemented as a Publisher that wraps the upstream Publisher.
 *      - To the upstream, log() looks like a Subscriber (because it subscribes to it and receives signals).
 *      - To the downstream, log() looks like a Publisher (because it propagates those signals further).
 */
public class LogOperator {

    private static final Logger log = LoggerFactory.getLogger(LogOperator.class);

    public static void main(String[] args) {

        Flux.range(1, 5)
                .log()
                .subscribe(DefaultSubscriber.create("sub1"));

        log.info("-----------------------------------------------------------------------");

        Flux.range(1, 5)
                .log("range-map")
                .map(i -> Util.faker().name().firstName())
                .log("map-subscribe")
                .subscribe(DefaultSubscriber.create("sub2"));
    }
}
