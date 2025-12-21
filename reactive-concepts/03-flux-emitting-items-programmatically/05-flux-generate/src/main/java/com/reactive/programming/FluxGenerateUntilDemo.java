package com.reactive.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class FluxGenerateUntilDemo {

    private static final Logger log = LoggerFactory.getLogger(FluxGenerateUntilDemo.class);

    public static void main(String[] args) {

        // Way 2
        Flux.<String>generate(synchronousSink -> {
            var country = Util.faker().country().name();
            synchronousSink.next(country);
        }).takeUntil(c -> c.equalsIgnoreCase("Canada")).subscribe(DefaultSubscriber.create("sub2"));


        // Way 1
        Flux.generate(synchronousSink -> {
            var country = Util.faker().country().name();
            synchronousSink.next(country);
            if(country.equalsIgnoreCase("Canada")) {
                synchronousSink.complete();
            }
        });
                //.subscribe(DefaultSubscriber.create("sub1"));

    }
}
