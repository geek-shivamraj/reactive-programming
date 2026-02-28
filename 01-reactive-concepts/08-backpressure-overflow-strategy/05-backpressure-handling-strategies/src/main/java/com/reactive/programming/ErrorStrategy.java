package com.reactive.programming;

import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 *  Error Strategy to handle Back Pressure
 *      - Immediately signals an error signal to downstream when overwhelmed. This will only observe & monitor
 *      - Pros: Fast failure, useful for strict reliability requirements.
 *          - Benefit: Immediate failure makes issues visible and forces corrective action.
 *      - Cons: Stream terminates; no recovery unless handled upstream.
 *      - Operator: onBackpressureError()
 *
 *  Use Case: When data loss is unacceptable, and you prefer to fail fast rather than risk silent drops or memory issues.
 *      - e.g., Financial transactions or mission-critical systems where missing data is catastrophic.
 *      - e.g., Strict SLAs where exceeding capacity must trigger alerts or retries.
 *      - e.g., Testing/debugging to detect bottlenecks early.
 */
public class ErrorStrategy {

    private static final Logger log = LoggerFactory.getLogger(ErrorStrategy.class);

    public static void main(String[] args) {

        var producer = Flux.create(sink -> {
                    for (int i = 1; i <= 500 && !sink.isCancelled(); i++) {
                        log.info("Generating: {}", i);
                        sink.next(i);
                        Util.sleepMilliseconds(50);
                    }
                    sink.complete();
                }).cast(Integer.class).subscribeOn(Schedulers.parallel());

        producer
                // Here we've to add method .onBackpressureError()
                .onBackpressureError()
                .limitRate(1)
                .publishOn(Schedulers.boundedElastic()).map(ErrorStrategy::timeConsumingTask)
                .subscribe();

        Util.sleepSeconds(60);
    }

    private static int timeConsumingTask(int i) {
        log.info("Received: {}", i);
        Util.sleepSeconds(1);
        return i;
    }

}
