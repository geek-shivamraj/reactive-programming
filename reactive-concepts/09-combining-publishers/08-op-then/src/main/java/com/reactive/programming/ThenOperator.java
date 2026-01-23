package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

/**
 *  then operator
 *      - Use when you're not interested in the result of a producer/chain multiple asynchronous calls to execute one by one.
 *      - For e.g., You're inserting a bunch of records into a database. You just need to know if it's success or not.
 *                  Not the intermediate results.
 */
public class ThenOperator {

    private static final Logger log = LoggerFactory.getLogger(ThenOperator.class);

    public static void main(String[] args) {

        var records = List.of("a", "b", "c");

        // Use case: Inserting records into a database & ignore the result.
        saveRecords(records)
                .then() // We can comment then() to see the difference
                ;//.subscribe(DefaultSubscriber.create("sub1"));

        saveRecords(records)
                .then(sendNotification(records))
                .subscribe(DefaultSubscriber.create("sub2"));

        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(3);

    }

    private static Flux<String> saveRecords(List<String> records) {
        return Flux.fromIterable(records)
                .map(r -> "saved " + r)
                .delayElements(Duration.ofMillis(500));
    }

    private static Mono<Void> sendNotification(List<String> records) {
        return Mono.fromRunnable(() -> log.info("All these {} records saved successfully", records));
    }
}
