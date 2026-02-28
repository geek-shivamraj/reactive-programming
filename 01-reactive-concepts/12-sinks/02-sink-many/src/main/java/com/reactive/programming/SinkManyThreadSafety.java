package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

/**
 *  Scenario:
 *      - We want to test if sink is thread safe or not i.e., once we're able to drop an item into the sink, then
 *          the Reactor will take it safely & give it to the arrayList.
 *      - Aim here is to test whether we can safely drop items into the sink from multiple threads.
 *
 *  demo1() - tryEmitNext
 *    Sink type matters:
 *      - If it’s a Sinks.One, only the first emission succeeds; all others fail with EmitResult.FAIL_TERMINATED.
 *      - If it’s a Sinks.Many (multicast/unicast/replay), we’re trying to push 1000 integers concurrently via tryEmitNext() method.
 *
 *    Concurrent Issue:
 *      - tryEmitNext is thread-safe but non-serialized.
 *      - If multiple threads emit at the same time, we may see EmitResult.FAIL_NON_SERIALIZED.
 *      - This is why Reactor provides emitNext(..., EmitFailureHandler) for controlled retries.
 *
 *    Outcome:
 *      - Some emissions succeed (SUCCESS).
 *      - Others may fail due to:
 *          - FAIL_NON_SERIALIZED (concurrent emission detected).
 *          - FAIL_OVERFLOW (if backpressure buffer is full).
 *          - FAIL_ZERO_SUBSCRIBER (if no one subscribed yet).
 *
 *  demo2() - emitNext
 *      - This is why Reactor provides emitNext(..., EmitFailureHandler) for controlled retries
 *          i.e., Retries if concurrency caused the failure.
 *
 *  Extract:
 *      - Reactor documentation says Sink is thread-safe & it is, but it will fail fast if multiple threads
 *          try to emit at the same time.
 *      - To handle this, we can use emitNext(..., EmitFailureHandler) for controlled retries.
 *
 */
public class SinkManyThreadSafety {

    private static final Logger log = LoggerFactory.getLogger(SinkManyThreadSafety.class);

    public static void main(String[] args) {

        demo1();

        log.info("------------------------------------------------------------");

        demo2();
    }

    private static void demo1() {

        // handle through which we would push items
        // onBackPressureBuffer - unbounded queue
        var sink = Sinks.many().unicast().onBackpressureBuffer();

        // handle through which subscriber will receive items
        var flux = sink.asFlux();

        var list = new ArrayList<>();
        flux.subscribe(list::add);

        for (int i = 0; i < 1000; i++) {
            var j = i;
            CompletableFuture.runAsync(() -> {
                // Here multiple threads are trying to drop items into the sink concurrently.
                log.info("Emitted Item: {}, EmitResult: {}", j, sink.tryEmitNext(j));
            });
        }

        Util.sleepSeconds(2);

        log.info("list size using tryEmitNext: {}", list.size());
    }

    private static void demo2() {

        // handle through which we would push items
        // onBackPressureBuffer - unbounded queue
        var sink = Sinks.many().unicast().onBackpressureBuffer();

        // handle through which subscriber will receive items
        var flux = sink.asFlux();

        var list = new ArrayList<>();
        flux.subscribe(list::add);

        for (int i = 0; i < 1000; i++) {
            var j = i;
            CompletableFuture.runAsync(() ->
                    sink.emitNext(j, (signal, emitResult) -> Sinks.EmitResult.FAIL_NON_SERIALIZED.equals(emitResult))
            );
        }

        Util.sleepSeconds(2);

        log.info("list size using emitNext: {}", list.size());
    }
}
