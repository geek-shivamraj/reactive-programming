package com.reactive.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

/**
 *  - Flux.create() is used to create a Flux programmatically & keep on emitting items till some condition is met.
 *  - Flux.create() gives us FluxSink object & we can use FluxSink inbuilt methods like next(), complete() etc.
 *      to keep on emitting items until some condition is met.
 *
 *  - Sometimes our business logic would be very simple like below then we can use below approach & sometimes we can
 *      have complex business logic.
 *      - Using below way, we might end up writing a lot of code inside lambda expression that might affect the readability.
 *      - We can follow a different approach to make the code more readable & maintainable.
 *
 */
public class FluxCreateDemo {

    private static final Logger log = LoggerFactory.getLogger(FluxCreateDemo.class);

    public static void main(String[] args) {
        Flux.create(fluxSink -> {
            fluxSink.next(1);
            fluxSink.next(2);
            fluxSink.complete();
        }).subscribe(DefaultSubscriber.create("sub1"));

        log.info("--------------------------------------------------");

        // We can keep on emitting items using a loop
        Flux.create(fluxSink -> {
            for (int i = 1; i <= 10; i++) {
                fluxSink.next(Util.faker().country().name());
            }
            fluxSink.complete();
        }).subscribe(DefaultSubscriber.create("sub2"));

        log.info("--------------------------------------------------");

        // Suppose we've a requirement to keep on emitting country names till you get "Canada"
        Flux.create(fluxSink -> {
                String countryName;
                do {
                    countryName = Util.faker().country().name();
                    fluxSink.next(countryName);
                } while (!countryName.equalsIgnoreCase("Canada"));
                fluxSink.complete();
        }).subscribe(DefaultSubscriber.create("sub3"));
    }
}
