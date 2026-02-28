package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

/**
 *  - We will observe here that now a new thread will execute flux.subscribe(), so, the new thread will ends up with executing the flux for sub1.
 */
public class PubSubNewThreads {

    private static final Logger log = LoggerFactory.getLogger(PubSubNewThreads.class);
    public static void main(String[] args) {

        log.info("*************** Starting Program ***************");

        var flux = Flux.create(sink -> {
            for (int i = 0; i < 2; i++) {
                log.info("Generating {}", i);
                sink.next(i);
            }
            sink.complete();
        }).doOnNext(i -> log.info("Value {}", i));

        // Here main thread will execute everything
        //flux.subscribe(DefaultSubscriber.create("sub1"));
        //flux.subscribe(DefaultSubscriber.create("sub2"));

        // Here instead of main thread, a new thread will execute everything
        Runnable runnable1 = () -> flux.subscribe(DefaultSubscriber.create("sub1"));
        Thread.ofPlatform().start(runnable1);

        log.info("*************** Ending Program ***************");
    }
}
