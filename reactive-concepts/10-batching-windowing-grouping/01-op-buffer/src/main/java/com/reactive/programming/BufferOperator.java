package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 *   - Buffer operators in Project Reactor allow you to collect emitted items into lists (batches) before passing them downstream.
 *   - They’re essential when you want to process data in chunks instead of item-by-item, which helps with performance, rate-limiting,
 *      or batch-oriented workflows.
 */
public class BufferOperator {

    private static final Logger log = LoggerFactory.getLogger(BufferOperator.class);

    public static void main(String[] args) {

        eventStream()
                .doOnNext(i -> log.info("Processing: {}", i))

                // By default, the buffer will wait for source to complete or integer MAX_VALUE
                //.buffer()

                // Buffer will wait for max of 3 items or source to complete
                //.buffer(3)

                // Buffer will wait for max of 500ms or source to complete
                .buffer(Duration.ofMillis(500))
                .subscribe(DefaultSubscriber.create("sub1"));

        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(15);

    }

    private static Flux<String> eventStream() {
        return Flux.interval(Duration.ofMillis(200))
                .take(10)
                .map(i -> "event - " + (i + 1));
    }
}
