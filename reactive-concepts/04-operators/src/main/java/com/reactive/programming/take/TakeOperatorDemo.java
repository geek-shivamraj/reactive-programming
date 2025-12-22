package com.reactive.programming.take;

import com.reactive.programming.helper.DefaultSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.stream.IntStream;

public class TakeOperatorDemo {

    private static final Logger log = LoggerFactory.getLogger(TakeOperatorDemo.class);

    public static void main(String[] args) {

        IntStream.range(1, 10)
                .limit(3)
                .forEach(System.out::println);

        log.info("--------------------------------------------------");

        // We can see logs to understand
        Flux.range(1, 10)
                .log("take-op-log")
                .take(3)
                .log("sub1-log")
                .subscribe(DefaultSubscriber.create("sub1"));

        log.info("--------------------------------------------------");
        // We can use takeWhile op to take elements until a condition is met & stop when condition is not met
        Flux.range(1, 10)
                .log("takeWhile-op-log")
                .takeWhile(i -> i < 5)
                .log("sub2-log")
                .subscribe(DefaultSubscriber.create("sub2"));

        log.info("--------------------------------------------------");
        /**
         *  - It emits items from the source until the predicate evaluates to true.
         *  - Importantly, the item that satisfies the predicate is included in the output, and then the sequence completes.
         */
        Flux.range(1, 10)
                .log("takeUntil-op-log")
                .takeUntil(i -> i == 5)
                .log("sub3-log")
                .subscribe(DefaultSubscriber.create("sub3"));
    }
}
