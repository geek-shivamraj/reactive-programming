package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 *  - Here we can see that the Producer adjusts the speed depending on the consumer processing speed.
 */
public class BackPressureMultipleSubs {

    private static final Logger log = LoggerFactory.getLogger(BackPressureMultipleSubs.class);

    public static void main(String[] args) {

        // Parallel Thread Pool
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

        // Bounded Elastic Thread Pool - Slow Subscriber
        producer
                .limitRate(5)
                .publishOn(Schedulers.boundedElastic())
                .map(BackPressureMultipleSubs::timeConsumingTask)
                .subscribe(DefaultSubscriber.create("sub1"));


        // Fast Subscriber
        producer
                .take(100)
                .publishOn(Schedulers.boundedElastic())
                .subscribe(DefaultSubscriber.create("sub2"));

        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(20);

    }

    private static int timeConsumingTask(int i) {
//        log.info("Time consuming task: {}", i);
        Util.sleepSeconds(1);
        return i;
    }
}
