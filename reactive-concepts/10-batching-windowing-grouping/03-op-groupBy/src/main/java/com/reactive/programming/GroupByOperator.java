package com.reactive.programming;

import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 *  - The groupBy operator in Project Reactor splits a Flux into multiple sub-streams (GroupedFlux), each keyed by a property.
 *  - It’s useful when you want to process elements by category (like logs by severity, orders by customer, or events by type)
 *      while still staying reactive.
 */
public class GroupByOperator {

    private static final Logger log = LoggerFactory.getLogger(GroupByOperator.class);

    public static void main(String[] args) {

        Flux.range(1, 30)
                .delayElements(Duration.ofSeconds(1))
                //.map(i -> i * 2)
                //.startWith(1)
                .groupBy(i -> i % 2)
                .flatMap(GroupByOperator::processEvents)
                .subscribe();

        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(35);
    }

    private static Mono<Void> processEvents(GroupedFlux<Integer, Integer> groupedFlux) {
        log.info("Received Flux for key: {}", groupedFlux.key());
        return groupedFlux.doOnNext(i -> log.info("Flux key: {}, item: {}", groupedFlux.key(), i))
                .doOnComplete(() -> log.info("Completed Flux for key: {}", groupedFlux.key()))
                .then();
    }


}
