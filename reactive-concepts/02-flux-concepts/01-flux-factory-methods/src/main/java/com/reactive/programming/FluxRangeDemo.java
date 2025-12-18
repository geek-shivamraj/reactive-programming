package com.reactive.programming;

import reactor.core.publisher.Flux;

/**
 *  - Flux.range() factory method creates a Flux from a range of numbers. Instead of for loop, we can use Flux.range()
 */
public class FluxRangeDemo {

    public static void main(String[] args) {

        Flux.range(1, 5)
                .subscribe(DefaultSubscriber.create("sub1"));

        Flux.range(3, 5)
                .subscribe(DefaultSubscriber.create("sub2"));

        // Generate 5 random first names.
        Flux.range(1, 5)
                .map(i -> Util.faker().name().firstName())
                .subscribe(DefaultSubscriber.create("sub3"));
    }
}
