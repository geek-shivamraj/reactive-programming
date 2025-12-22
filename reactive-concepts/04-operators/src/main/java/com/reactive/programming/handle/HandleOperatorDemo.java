package com.reactive.programming.handle;

import com.reactive.programming.helper.DefaultSubscriber;
import reactor.core.publisher.Flux;

/**
 *  - Handle behaves like filter + map
 */
public class HandleOperatorDemo {

    public static void main(String[] args) {

        Flux<Integer> flux = Flux.range(1, 10);

        Flux<Integer> flux1 = flux.handle((item, sink) -> {
            sink.error(new RuntimeException("oops"));
        });

        //flux.subscribe(DefaultSubscriber.create("sub1"));
        //flux1.subscribe(DefaultSubscriber.create("sub2"));

        /**
         *  Requirement:
         *      - If input == 1, then emit -2
         *      - If input == 4, don't emit
         *      - If input == 7, emit error
         *      - otherwise emit the value as it is.
         */

        // Way 1
        Flux<Integer> flux2 = Flux.range(1, 10)
                .handle((item, sink) -> {
                    switch (item) {
                        case 1 -> sink.next(-2);
                        case 4 -> {}
                        case 7 -> sink.error(new RuntimeException("oops"));
                        default -> sink.next(item);
                    }
                });
        //flux2.subscribe(DefaultSubscriber.create("sub3"));

        // Way 2
        Flux.range(1, 10)
                // Filter out 7 that's emitting error
                .filter(i -> i != 7)
                .handle((item, sink) -> {
                    switch (item) {
                        case 1 -> sink.next(-2);
                        case 4 -> {}
                        case 7 -> sink.error(new RuntimeException("oops"));
                        default -> sink.next(item);
                    }
                })
                // Optionally we can cast
                .cast(Integer.class)
                .subscribe(DefaultSubscriber.create("sub4"));

    }
}
