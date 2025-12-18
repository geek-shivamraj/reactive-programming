package com.reactive.programming;

import reactor.core.publisher.Flux;

import java.util.List;

/**
 *  - We can create a Flux from a Stream using "fromStream()" factory method.
 */
public class FluxFromStreamDemo {

    public static void main(String[] args) {

        var list = List.of(1, 2, 3, 4, 5, 6);
        var stream = list.stream();

        //stream.forEach(System.out::println);
        //stream.forEach(System.out::println); // Error: stream has already been operated upon or closed

        Flux.fromStream(stream);
                //.subscribe(DefaultSubscriber.create("sub1"));

        /**
         *  - We will get Error: java.lang.IllegalStateException: stream has already been operated upon or closed
         *      - Stream is a one time use object & once it is used/consumed, it can't be used again. This is how java stream
         *          works.
         *
         *  - Solution:
         *      - If you've a stream & you expect multiple subscriber, then you've to provide the supplier of stream.
         */
        Flux.fromStream(stream);
               // .subscribe(DefaultSubscriber.create("sub2"));

        // Solution
        Flux.fromStream(() -> list.stream())
                .subscribe(DefaultSubscriber.create("sub1"));

        Flux.fromStream(() -> list.stream())
                .subscribe(DefaultSubscriber.create("sub2"));
    }
}
