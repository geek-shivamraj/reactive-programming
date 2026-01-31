package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Sinks;

/**
 *  We can emit multiple messages but there will be only one subscriber
 */
public class SinkManyUnicast {

    private static final Logger log = LoggerFactory.getLogger(SinkManyUnicast.class);

    public static void main(String[] args) {
        //demo1();
        demo2();
    }

    // Single subscriber
    private static void demo1() {

        var sink = Sinks.many().unicast().onBackpressureBuffer();

        var flux = sink.asFlux();

        sink.tryEmitNext("One");
        sink.tryEmitNext("Two");
        sink.tryEmitNext("Three");

        flux.subscribe(DefaultSubscriber.create("sub1"));
    }

    // Multiple subscribers
    private static void demo2() {

        var sink = Sinks.many().unicast().onBackpressureBuffer();

        var flux = sink.asFlux();

        sink.tryEmitNext("One");
        sink.tryEmitNext("Two");
        sink.tryEmitNext("Three");

        flux.subscribe(DefaultSubscriber.create("sub1"));
        flux.subscribe(DefaultSubscriber.create("sub2"));
    }

}
