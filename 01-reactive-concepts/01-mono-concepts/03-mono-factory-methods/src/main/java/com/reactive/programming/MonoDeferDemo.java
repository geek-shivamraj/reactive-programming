package com.reactive.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 *  - Creating Publisher is a lightweight operation & executing time-consuming business logic can be delayed by using
 *      fromSupplier, fromCallable, fromRunnable, fromFuture.
 *  - However, if we want to delay the Publisher creation, then we can use Mono.defer()
 */
public class MonoDeferDemo {

    private static final Logger log = LoggerFactory.getLogger(MonoDeferDemo.class);

    public static void main(String[] args) {

        // Here even without subscriber, createPublisher() takes 2 seconds.
        createPublisher();
                //.subscribe(DefaultSubscriber.create("sub1"));

        log.info("--------------------------------------------------------------");

        // To delay this createPublisher, we can use Mono.defer()
        Mono.defer(MonoDeferDemo::createPublisher);
                //.subscribe(DefaultSubscriber.create("sub2"));
    }

    /**
     *  - Usually we assume that creating publisher is not a time-consuming operation.
     *  - Let's say creating publisher is time-consuming operation.
     */
    private static Mono<Integer> createPublisher() {
        log.info("Creating publisher...");
        var list = List.of(1, 2, 3, 4);
        Util.sleepSeconds(2);
        return Mono.fromSupplier(() -> sum(list));
    }

    // Time-consuming business logic
    private static int sum(List<Integer> list) {
        log.info("Finding the sum of {}", list);
        Util.sleepSeconds(3);
        return list.stream().reduce(0, Integer::sum);
    }
}
