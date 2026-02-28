package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *  collectList operator
 *      - To collect the items received via Flux. Assuming we'll have finite items!
 *      - It's not blocking. collectList op will try to collect the items internally and emit a single list on complete signal.
 *      - If there is an error, then error will be emitted.
 */
public class CollectListOperator {

    public static void main(String[] args) {

        Flux.range(1, 10)
                //.concatWith(Mono.error(new RuntimeException("oopss error")))
                .collectList()
                .subscribe(DefaultSubscriber.create("sub1"));

        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(3);

    }
}
