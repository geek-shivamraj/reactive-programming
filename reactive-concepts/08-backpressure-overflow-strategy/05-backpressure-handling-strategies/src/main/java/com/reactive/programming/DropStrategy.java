package com.reactive.programming;

import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 *  Drop Strategy to handle backpressure
 *      - Drops items when the subscriber cannot keep up with Producer.
 *      - Pros: Prevents memory overflow while keeping system responsive.
 *      - Cons: Data loss; only suitable when occasional loss is acceptable (e.g., telemetry).
 *      - Operator: onBackpressureDrop()
 *
 *  - In below example, the operator will keep on dropping all the item producer produced without any proper
 *      request from downstream.
 *
 *  Use case: When occasional data loss is acceptable and system stability is more important than completeness.
 *      - e.g., Telemetry/metrics collection (dropping some sensor readings is fine)
 *      - e.g., Real-time monitoring dashboards where approximate values suffice.
 *      - e.g., IoT streams with high-frequency events where only trends matter.
 *
 */
public class DropStrategy {

    private static final Logger log = LoggerFactory.getLogger(DropStrategy.class);

    public static void main(String[] args) {

        var producer = Flux.create(sink -> {
                    for (int i = 1; i <= 500 && !sink.isCancelled(); i++) {
                        log.info("Generating: {}", i);
                        sink.next(i);
                        Util.sleepMilliseconds(50);
                    }
                    sink.complete();
                })
                .cast(Integer.class).subscribeOn(Schedulers.parallel());

        producer
                // Here we've to add method .onBackpressureDrop()
                .onBackpressureDrop()
                .log()
                .limitRate(1)
                .publishOn(Schedulers.boundedElastic())
                .map(DropStrategy::timeConsumingTask)
                .subscribe();

        Util.sleepSeconds(60);
    }

    private static int timeConsumingTask(int i) {
        log.info("Received: {}", i);
        Util.sleepSeconds(1);
        return i;
    }
}
