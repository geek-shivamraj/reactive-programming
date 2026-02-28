package com.reactive.programming;

import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 *  Fixed Size Buffer Strategy to handle backpressure
 *      - We use onBackpressureBuffer(int capacity, ... )
 *      - Buffers up to a fixed size & if the producer emits more than that before the consumer catches up, apply an overflow strategy
 *          or else we will receive error.
 *      - Overflow Strategies:
 *          - ERROR → throw MissingBackpressureException and terminate.
 *          - DROP_OLDEST → discard the oldest element in the buffer to make room for the new one.
 *          - DROP_LATEST → discard the incoming element (keep the buffer as-is).
 *          - CUSTOM HANDLER → you can log or handle dropped elements explicitly.
 */
public class FixedSizeBufferStrategy {

    private static final Logger log = LoggerFactory.getLogger(FixedSizeBufferStrategy.class);

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
                // Here we've to add method .onBackPressureBuffer(int capacity)
                .onBackpressureBuffer(10) // 2-11 items will be stored & on 12 items, we will get error signal.
                .limitRate(1)
                .publishOn(Schedulers.boundedElastic())
                .map(FixedSizeBufferStrategy::timeConsumingTask)
                .subscribe();

        Util.sleepSeconds(60);
    }

    private static int timeConsumingTask(int i) {
        log.info("Received: {}", i);
        Util.sleepSeconds(1);
        return i;
    }
}
