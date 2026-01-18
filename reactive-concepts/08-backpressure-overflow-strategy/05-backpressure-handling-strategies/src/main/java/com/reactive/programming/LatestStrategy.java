package com.reactive.programming;

import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 *  Latest Strategy to handle backpressure
 *      - Keeps only the most recent item in case downstream might request & discarding older ones.
 *      - Pros: Useful for UI updates or sensor data where only the latest value matters.
 *      - Cons: Older data is lost.
 *      - Operator: onBackpressureLatest()
 */
public class LatestStrategy {

    private static final Logger log = LoggerFactory.getLogger(LatestStrategy.class);

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
                // Here we've to add method .onBackpressureLatest()
                .onBackpressureLatest()
                .log()
                .limitRate(1)
                .publishOn(Schedulers.boundedElastic())
                .map(LatestStrategy::timeConsumingTask)
                .subscribe();

        Util.sleepSeconds(60);
    }

    private static int timeConsumingTask(int i) {
        log.info("Received: {}", i);
        Util.sleepSeconds(1);
        return i;
    }
}
