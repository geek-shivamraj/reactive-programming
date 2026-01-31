package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Sinks;

import java.time.Duration;

/**
 *  demo1()
 *      - We can see that sub1 is fast sub & sub2 is slow sub & becoz of slow sub, fast sub performing is also getting impact.
 *      - Problem:
 *          - If one sub is slow then it will impact the performance of fast sub. Ideally if sub1 is fast then sub1 should get message quickly.
 *          - After emitting 16 items, the buffer gets full & we will get FAIL_OVERFLOW for other items. Once all items are emitted
 *              then the subscribers will start consuming from buffer (till buffer size).
 *      - Solutions:
 *          - Solution 1: Increase the buffer size. (demo2)
 *              - Problem: Performance impact will still be there as fast sub will get message at the same time as slow sub.
 *                  Ideally if sub1 is fast then sub1 should get message quickly.
 *
 *          - Solution 2: Add explicit buffer for slow subscriber (demo3)
 *              - No performance impact for fast sub & slow sub can consume messages from its own buffer.
 *
 *          - Solution 3: use .directBestEffort() instead of.onBackpressureBuffer() to focus only on the fast subscriber
 *              & use .onBackpressureBuffer() for the slow subscribers (demo4)
 *              - No performance impact for fast sub & slow sub can consume messages from its own buffer.
 *
 */
public class SinkManyMulticastDirectBestEffort {

    private static final Logger log = LoggerFactory.getLogger(SinkManyMulticastDirectBestEffort.class);

    public static void main(String[] args) {

        //demo1();

        //demo2();

        //demo3();

        demo4();

        Util.sleepSeconds(30);
    }

    private static void demo1() {

        System.setProperty("reactor.bufferSize.small", "16");

        // handle through which we would push items
        // onBackPressureBuffer - bounded queue
        var sink = Sinks.many().multicast().onBackpressureBuffer();

        // handle through which subscriber will receive items
        var flux = sink.asFlux();

        // fast subscriber
        flux.subscribe(DefaultSubscriber.create("sub1"));

        // slow subscriber
        flux.delayElements(Duration.ofSeconds(1)).subscribe(DefaultSubscriber.create("sub2"));

        for (int i = 1; i <= 50; i++) {
            var result = sink.tryEmitNext(i);
            log.info("Item: {}, result: {}", i, result);
        }
    }

    // Solution 1: Increase buffer size
    private static void demo2() {

        // Increase buffer size
        System.setProperty("reactor.bufferSize.small", "101");

        var sink = Sinks.many().multicast().onBackpressureBuffer();
        var flux = sink.asFlux();

        flux.subscribe(DefaultSubscriber.create("sub1"));
        flux.delayElements(Duration.ofSeconds(1)).subscribe(DefaultSubscriber.create("sub2"));

        for (int i = 1; i <= 100; i++) {
            var result = sink.tryEmitNext(i);
            log.info("Item: {}, result: {}", i, result);
        }
    }

    // Solution 2: Add explicit buffer for slow subscriber
    private static void demo3() {

        System.setProperty("reactor.bufferSize.small", "16");

        var sink = Sinks.many().multicast().onBackpressureBuffer();
        var flux = sink.asFlux();

        flux.subscribe(DefaultSubscriber.create("sub1"));
        flux.onBackpressureBuffer().delayElements(Duration.ofSeconds(1)).subscribe(DefaultSubscriber.create("sub2"));

        for (int i = 1; i <= 100; i++) {
            var result = sink.tryEmitNext(i);
            log.info("Item: {}, result: {}", i, result);
        }
    }

    // Solution 3: use .directBestEffort() instead of.onBackpressureBuffer() to focus only on the fast subscriber
    // & use .onBackpressureBuffer() for the slow subscribers
    private static void demo4() {

        System.setProperty("reactor.bufferSize.small", "16");

        // Here focus only on the fast subscriber
        var sink = Sinks.many().multicast().directBestEffort();
        var flux = sink.asFlux();

        flux.subscribe(DefaultSubscriber.create("sub1"));

        // Adding onBackpressureBuffer() for the slow subscriber
        flux.onBackpressureBuffer().delayElements(Duration.ofSeconds(1)).subscribe(DefaultSubscriber.create("sub2"));

        for (int i = 1; i <= 100; i++) {
            var result = sink.tryEmitNext(i);
            log.info("Item: {}, result: {}", i, result);
        }
    }
}
