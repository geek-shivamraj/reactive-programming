package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

/**
 *  - We will observe here that since main thread is the one which is executing flux.subscribe(), it ends up with executing everything here.
 */
public class PubSubDefaultThread {

    private static final Logger log = LoggerFactory.getLogger(PubSubDefaultThread.class);
    public static void main(String[] args) {

        log.info("*************** Starting Program ***************");

        var flux = Flux.create(sink -> {
            for (int i = 0; i < 2; i++) {
                log.info("Generating {}", i);
                sink.next(i);
            }
            sink.complete();
        }).doOnNext(i -> log.info("Value {}", i));

        flux.subscribe(DefaultSubscriber.create("sub1"));
        flux.subscribe(DefaultSubscriber.create("sub2"));

        log.info("*************** Ending Program ***************");
    }
}
