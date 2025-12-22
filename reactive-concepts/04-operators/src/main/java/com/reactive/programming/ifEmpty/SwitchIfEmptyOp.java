package com.reactive.programming.ifEmpty;

import com.reactive.programming.helper.DefaultSubscriber;
import reactor.core.publisher.Flux;

/**
 *  - switchIfEmpty
 *      - switchIfEmpty is similar to onErrorResume
 *      - If something is empty & we want to switch to another publisher, then switchIfEmpty operator will be useful.
 *
 *  - Use cases:
 *      - MySQL + Redis cache. In case not available in case, call the DB.
 */
public class SwitchIfEmptyOp {

    public static void main(String[] args) {

        Flux.range(1, 10)
                .filter(i -> i > 11)
                .switchIfEmpty(fallback())
                .subscribe(DefaultSubscriber.create("sub1"));
    }

    private static Flux<Integer> fallback() {
        return Flux.range(100, 3);
    }
}
