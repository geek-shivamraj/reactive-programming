package com.reactive.programming;

import reactor.core.publisher.Flux;

import java.util.List;

/**
 *  - We can create a Flux from an Array using "fromArray()" factory method & from iterables like list
 *      using "fromIterable()" factory method.
 */
public class FluxFromIterableOrArrayDemo {

    public static void main(String[] args) {

        Integer[] arr = {1, 2, 3, 4, 5, 6};
        Flux.fromArray(arr)
                .subscribe(DefaultSubscriber.create("sub1"));

        List<Integer> list = List.of(1, 2, 3, 4, 5, 6);
        Flux.fromIterable(list)
                .subscribe(DefaultSubscriber.create("sub2"));
    }
}
