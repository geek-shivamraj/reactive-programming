package com.reactive.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 *  - Creating Publisher & Executing it is not same.
 *      - Creating a publisher is a very lightweight operation.
 * 		- Time Consuming operation inside the publisher will be executed only when the publisher is subscribed.
 *  - So, when we use Mono.fromSupplier, Mono.fromCallable, Mono.fromRunnable, Mono.fromFuture we delay the execution of the publisher
 *      till its subscribed.
 */
public class PublisherCreateVsExecuteDemo {

    private static final Logger log = LoggerFactory.getLogger(PublisherCreateVsExecuteDemo.class);

    public static void main(String[] args) {

        // Step 1: Publisher creation
        getName();

        log.info("------------------------------------------------");

        // Step 2: Publisher execution
        getName().subscribe(DefaultSubscriber.create("sub1"));
    }

    private static Mono<String> getName() {
        log.info("Entered the method");

        // below we're simply creating the publisher & not doing any operation.
        return Mono.fromSupplier(() -> {
            log.info("Generating name....");
            Util.sleepSeconds(3);
            return Util.faker().name().fullName();
        });
    }
}
