package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Sinks;

/**
 *  - Here we're using single thread so no need to use emitNext().
 *  - Many - multicast: Late subscriber can't see the message.
 */
public class SinkManyMulticast {

    private static final Logger log = LoggerFactory.getLogger(SinkManyMulticast.class);

    public static void main(String[] args) {

        //demo1();

        demo2();
    }

    private static void demo1() {
        // handle through which we would push items
        // onBackPressureBuffer - bounded queue
        var sink = Sinks.many().multicast().onBackpressureBuffer();

        // handle through which subscriber will receive items
        var flux = sink.asFlux();

        flux.subscribe(DefaultSubscriber.create("sub1"));
        flux.subscribe(DefaultSubscriber.create("sub2"));

        sink.tryEmitNext("One");
        sink.tryEmitNext("Two");
        sink.tryEmitNext("Three");

        Util.sleepSeconds(2);

        flux.subscribe(DefaultSubscriber.create("sub3"));
        sink.tryEmitNext("Four");
    }

    // multicast has something called "warmup" behavior
    // Here only the 1st subscriber will get all the messages from the queue & late subscriber won't get any message.
    private static void demo2() {
        // handle through which we would push items
        // onBackPressureBuffer - bounded queue
        var sink = Sinks.many().multicast().onBackpressureBuffer();

        // handle through which subscriber will receive items
        var flux = sink.asFlux();

        sink.tryEmitNext("One");
        sink.tryEmitNext("Two");
        sink.tryEmitNext("Three");

        Util.sleepSeconds(2);

        flux.subscribe(DefaultSubscriber.create("sub1"));
        flux.subscribe(DefaultSubscriber.create("sub2"));
        flux.subscribe(DefaultSubscriber.create("sub3"));

        sink.tryEmitNext("Four");
    }
}
