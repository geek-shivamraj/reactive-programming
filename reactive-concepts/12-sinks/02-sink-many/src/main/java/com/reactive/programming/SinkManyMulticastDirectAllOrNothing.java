package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Sinks;

import java.time.Duration;

/**
 *  .directAllOrNothing()
 *      - If any subscriber is slow, don't deliver to other subscribers unlike .directBestEffort() focuses on fast subscriber.
 *
 */
public class SinkManyMulticastDirectAllOrNothing {

    private static final Logger log = LoggerFactory.getLogger(SinkManyMulticastDirectAllOrNothing.class);

    public static void main(String[] args) {

        demo1();
    }

    private static void demo1() {

        System.setProperty("reactor.bufferSize.small", "16");

        // Here focus only on the fast subscriber
        var sink = Sinks.many().multicast().directAllOrNothing();
        var flux = sink.asFlux();

        flux.subscribe(DefaultSubscriber.create("sub1"));
        flux.delayElements(Duration.ofSeconds(1)).subscribe(DefaultSubscriber.create("sub2"));

        for (int i = 1; i <= 100; i++) {
            var result = sink.tryEmitNext(i);
            log.info("Item: {}, result: {}", i, result);
        }
    }
}
