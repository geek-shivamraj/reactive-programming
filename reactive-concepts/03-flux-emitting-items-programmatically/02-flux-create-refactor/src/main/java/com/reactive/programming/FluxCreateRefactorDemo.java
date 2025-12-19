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
 *  Different Approach:
 *      - We can create a separate class implementing Consumer<FluxSink<T>> & then we can use it inside Flux.create()
 *      - This way we can move the business logic inside a separate class & make the code more readable & maintainable.
 *
 */
public class FluxCreateRefactorDemo {

    private static final Logger log = LoggerFactory.getLogger(FluxCreateRefactorDemo.class);

    public static void main(String[] args) {
        var generator = new NameGenerator();
        var flux = Flux.create(generator);

        flux.subscribe(DefaultSubscriber.create("sub1"));

        for (int i = 0; i < 10; i++) {
            generator.generate();
        }
    }
}
