package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 *  - For Flux.generate(), the Reactor controls the loop, and it invokes again & again based on the downstream demand.
 *  - So, here Reactor understands that the Consumer is slow so, the producer has to adjust its speed accordingly.
 *
 *  - For Flux.create(), there is no automatic backpressure handling i.e., reactor provides sink & we're the one who controls the loop.
 *
 *  - Here we will observe that the consumer after consuming 16 items will not consume anything. Why is this
 *      - It's becoz after storing 16 items onto the queue, the Producer realizes that the consumer is too slow
 *          so it will start managing the queue internally & it will not hand over the items to the consumer, actually.
 *      - So, this is the Problem. Flux.generate() handles backpressure/speed automatically but with Flux.create(), it's very difficult
 *          to handle these kind of things. So, here everything goes to a separate internal queue & it will not give the items to the subscriber.
 *
 *  - So, when the consumer is too slow & the producer is too fast then particularly with Flux.create(), there is no automatic backpressure handling.
 */
public class FluxCreateBackPressureProblem {

    private static final Logger log = LoggerFactory.getLogger(FluxCreateBackPressureProblem.class);

    public static void main(String[] args) {
        System.setProperty("reactor.bufferSize.small", "16");
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
                .publishOn(Schedulers.boundedElastic())
                .map(FluxCreateBackPressureProblem::timeConsumingTask)
                .subscribe();
        Util.sleepSeconds(60);
    }

    private static int timeConsumingTask(int i) {
        log.info("Received: {}", i);
        Util.sleepSeconds(1);
        return i;
    }
}
