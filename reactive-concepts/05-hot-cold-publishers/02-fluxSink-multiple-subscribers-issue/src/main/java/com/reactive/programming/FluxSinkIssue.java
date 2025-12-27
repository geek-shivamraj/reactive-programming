package com.reactive.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;

/**
 *  - If we have multiple subscribers for the same FluxSink, then we will see the second subscriber will emit the items. Why ?
 *      - Refer NameGenerator class.
 *      - Here we've created just one instance of NameGenerator shared with multiple threads & this instance is accepting 2 different FluxSink
 *          & becoz of that we lost the old fluxSink.
 *
 *      - That's why fluxSink works for single subscriber.
 */
public class FluxSinkIssue {

    private static final Logger log = LoggerFactory.getLogger(FluxSinkIssue.class);

    public static void main(String[] args) {

        // NameGenerator is a consumer of FluxSink
        NameGenerator generator = new NameGenerator();
        var flux = Flux.create(generator);

        flux.subscribe(DefaultSubscriber.create("sub1"));
        flux.subscribe(DefaultSubscriber.create("sub2"));

        for (int i = 0; i < 10; i++) {
            generator.generate();
        }
    }
}
