package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 *  - By using limitRate() operator, we can tell Producer to just produce specific no. of items.
 */
public class LimitRateDemo {

    private static final Logger log = LoggerFactory.getLogger(LimitRateDemo.class);

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

        // Bounded Elastic Thread Pool
        producer
                // Limiting no. of items to be produced
                .limitRate(5)
                //.limitRate(1)
                .publishOn(Schedulers.boundedElastic())
                .map(LimitRateDemo::timeConsumingTask)
                .subscribe(DefaultSubscriber.create("sub1"));

        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(20);

    }

    private static int timeConsumingTask(int i) {
//        log.info("Time consuming task: {}", i);
        Util.sleepSeconds(1);
        return i;
    }
}
