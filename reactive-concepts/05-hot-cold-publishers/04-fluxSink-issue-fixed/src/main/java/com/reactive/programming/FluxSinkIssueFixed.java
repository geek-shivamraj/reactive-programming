package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

/**
 *  - Flux.create() produces a Cold Publisher by default (where each subscriber will have its own FluxSink).
 *  - We can make it Hot Publisher by using share() operator (Now we will have just one fluxSink for all subscribers).
 *
 */
public class FluxSinkIssueFixed {

    private static final Logger log = LoggerFactory.getLogger(FluxSinkIssueFixed.class);
    public static void main(String[] args) {

        NameGenerator generator = new NameGenerator();
        log.info("NameGenerator instance created: [{}]", generator.hashCode());

        var flux = Flux.create(generator).share();
        log.info("Flux created: {}", flux);

        flux.subscribe(DefaultSubscriber.create("sub1"));
        flux.subscribe(DefaultSubscriber.create("sub2"));

        for (int i = 0; i < 10; i++) {
            generator.generate();
        }
    }
}
