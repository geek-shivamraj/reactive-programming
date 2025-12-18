package com.reactive.programming;

import reactor.core.publisher.Flux;

/**
 *  - We can have multiple subscribers for a publisher & each subscriber have different requirement for which
 *      we can use filter(), map(), etc. i.e., transform the data as per subscriber requirement.
 */
public class MultipleSubscribers {

    public static void main(String[] args) {

        var flux = Flux.just(1, 2, 3, 4, 5, 6);

        flux.subscribe(DefaultSubscriber.create("sub1"));

        // sub2 is interested in elements greater than 7
        flux
                .filter(i -> i > 7)
                .subscribe(DefaultSubscriber.create("sub2"));

        // sub3 is interested in even numbers & map "a" as postfix to each element
        flux
                .filter(i -> i % 2 == 0)
                .map(i -> i + "a")
                .subscribe(DefaultSubscriber.create("sub3"));
    }
}
