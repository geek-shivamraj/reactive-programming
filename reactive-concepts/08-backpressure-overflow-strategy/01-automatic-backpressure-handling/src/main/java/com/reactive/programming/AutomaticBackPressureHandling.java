package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.util.concurrent.Queues;

import java.util.Queue;

/**
 *  - Reactor automatically handles the backpressure.
 *  - System.setProperty("reactor.bufferSize.smal", "16")
 *
 *      Here Flux.generate() will keep on emitting items forever, but We will observe that the Producer will stop after emitting 256 items.
 *          - It will wait till 75% of the items are consumed by a subscriber, post that, Flux.generate() will emit more items.
 *
 *      Question: Where is this 256 coming from ?
 *      Ans: Reactor maintains some queues(reactor.util.concurrent.Queues) internally to handle the backpressure.
 *          - public static final int SMALL_BUFFER_SIZE = Math.max(16, Integer.parseInt(System.getProperty("reactor.bufferSize.small", "256")));
 *
 *      - We can adjust the SMALL_BUFFER_SIZE by setting the system property, but we can't decrease below 16.
 *          - System.setProperty("reactor.bufferSize.small", "16");
 *          - We don't really have to worry about these numbers.
 *
 *      - So instead of Producer producing infinite amount of items & get in out of memory error, Reactor handles back pressure using Queue,
 *          once the queue is full, Producer will stop producing till 75% of items from the Queue will be consumed by a Consumer.
 *
 */
public class AutomaticBackPressureHandling {

    private static final Logger log = LoggerFactory.getLogger(AutomaticBackPressureHandling.class);

    public static void main(String[] args) {

        // Set the system property to adjust the SMALL_BUFFER_SIZE (Default is 256)
        System.setProperty("reactor.bufferSize.small", "16");

        var producer = Flux.generate(
                        () -> 1,
                        (state, sink) -> {
                            log.info("Generating: {}", state);
                            sink.next(state);
                            return ++state;
                        }
                )
                .cast(Integer.class)
                .subscribeOn(Schedulers.parallel());

        producer
                .publishOn(Schedulers.boundedElastic())
                .map(AutomaticBackPressureHandling::timeConsumingTask)
                .subscribe(DefaultSubscriber.create("sub1"));

        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(10);

    }

    private static int timeConsumingTask(int i) {
        log.info("Time consuming task: {}", i);
        Util.sleepMilliseconds(50);
        return i;
    }
}
