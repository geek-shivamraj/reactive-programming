package com.reactive.programming;

import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 *  Buffer Strategy to handle Back Pressure
 *      - Emitted items are stored in an internal buffer till the subscriber requests them.
 *      - Pros: Guarantees no data loss
 *      - Cons: Risk of OutOfMemoryError if the producer is much faster than consumer.
 *      - Operator: onBackpressureBuffer()
 *
 *      - In this example, the consumer will consume the items as per its speed & at the same time, the producer will emit items
 *          as per its speed & these items will be stored in internal buffer.
 *
 *  Use cases: When all data must be preserved and consumer lag is temporary.
 *      - e.g.1., If we've some occasional spikes for the Producer emitting data like user click stream etc., all the emitted items
 *          can be buffered meanwhile the subscriber can process according to its steady processing speed & will eventually catch up.
 *
 */
public class BufferStrategy {

    private static final Logger log = LoggerFactory.getLogger(BufferStrategy.class);

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
                // Here we've to add method .onBackPressureBuffer()
                .onBackpressureBuffer()
                .limitRate(1)
                .publishOn(Schedulers.boundedElastic())
                .map(BufferStrategy::timeConsumingTask)
                .subscribe();
        Util.sleepSeconds(60);
    }

    private static int timeConsumingTask(int i) {
        log.info("Received: {}", i);
        Util.sleepSeconds(1);
        return i;
    }
}
