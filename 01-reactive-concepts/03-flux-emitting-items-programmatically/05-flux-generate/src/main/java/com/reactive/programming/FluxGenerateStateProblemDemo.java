package com.reactive.programming;

import reactor.core.publisher.Flux;

/**
 *  - This is good but now Business requirement is to only emit 10 values or until we get Canada.
 *      - As a producer, it needs to stop emitting if certain condition is met or if downstream cancels the subscription or emit error.
 *      - So, we need to maintain state using Atomic reference (that's not reliable solution)
 *
 *  - Flux.generate() is good, but it's completely stateless i.e., the lambda expression will be invoked again & again
 *      based on the downstream demand.
 *  - But in some cases, we might want to maintain a state. For e.g., we've to open DB connection, query some records,
 *      then emit & close the connection.
 *      - Reactor provided some overloaded options (Flux.generate() with 3 params)
 *          - 1st param to provide the initial value (invoked only once) & that initialValue object is passed to 2nd param
 *          - 2nd param will process the initialValue object & return the object. (invoked based on the downstream demand)
 *          - 3rd param will be used to close the connection (invoked only once) - onComplete, onError, onCancel.
 *
 *
 */
public class FluxGenerateStateProblemDemo {

    public static void main(String[] args) {

        // Solution
        Flux.generate(
                () -> 0,
                (counter, sink) -> {
                    var country = Util.faker().country().name();
                    sink.next(country);
                    if(counter == 10 || country.equalsIgnoreCase("Canada")) {
                        sink.complete();
                    }
                    return counter + 1;
                }
        ).subscribe(DefaultSubscriber.create("sub2"));


        // This is good but now Business requirement is to only emit 10 values or until we get Canada.
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
