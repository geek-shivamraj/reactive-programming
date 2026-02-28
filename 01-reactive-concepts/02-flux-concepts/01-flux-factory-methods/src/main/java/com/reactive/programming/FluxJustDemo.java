package com.reactive.programming;

import reactor.core.publisher.Flux;

import java.util.List;

/**
 *  - List has a factory method "of()" using which we can provide arbitrary items to create a list,
 *      similarly Flux has "just()" factory method to create a Flux.
 */
public class FluxJustDemo {

    public static void main(String[] args) {
        //List list = List.of(1, 2, 3, "sam", "alex");

        Flux.just(1, 2, 3, "sam", "alex")
                .subscribe(DefaultSubscriber.create("sub1"));
    }
}
