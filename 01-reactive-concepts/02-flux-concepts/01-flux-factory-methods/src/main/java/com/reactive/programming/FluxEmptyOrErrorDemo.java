package com.reactive.programming;

import reactor.core.publisher.Flux;

public class FluxEmptyOrErrorDemo {

    public static void main(String[] args) {

        Flux.empty()
                .subscribe(DefaultSubscriber.create("sub1"));

        Flux.error(new RuntimeException("oops"))
                .subscribe(DefaultSubscriber.create("sub2"));
    }
}
