package com.reactive.programming.ifEmpty;

import com.reactive.programming.helper.DefaultSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *  - defaultIfEmpty
 *      - defaultIfEmpty is helpful to provide a default value in case the stream is empty.
 *      - It's similar to Optional's orElse or similar to onErrorReturn
 */
public class DefaultIfEmptyOp {

    public static void main(String[] args) {

        Mono.empty()
                .defaultIfEmpty("fallback")
                .subscribe(DefaultSubscriber.create("sub1"));

        System.out.println("----------------------------------------------------------------------------");

        Flux.range(1, 10)
                .filter(i -> i > 11)
                .defaultIfEmpty(-1)
                .subscribe(DefaultSubscriber.create("sub2"));
    }
}
