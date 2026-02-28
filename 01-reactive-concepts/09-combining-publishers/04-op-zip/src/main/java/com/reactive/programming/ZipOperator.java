package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 *  zip operator (it's either all or nothing i.e., item from all producers at the same time)
 *      - merge on other hand, doesn't wait for all producers to emit an item. so, no assembly here.
 *      - zip is an operator that combines multiple publishers (Flux/Mono) into a single publisher by pairing their emissions together.
 *      - Behavior:
 *          - It waits until each source has emitted an element.
 *          - Then it combines those elements into a tuple (or via a combinator function).
 *          - The resulting stream emits these combined values sequentially.
 *      - Key Point: It’s like a zipper — two (or more) streams interlock element by element.
 *
 */
public class ZipOperator {

    record Car(String body, String engine, String tires, Integer passengers){}

    public static void main(String[] args) {

        Flux.zip(getBody(), getEngine(), getTires(), getPassengers())
                .map(t -> new Car(t.getT1(), t.getT2(), t.getT3(), t.getT4()))
                .subscribe(DefaultSubscriber.create("sub1"));

        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(3);

    }

    private static Flux<String> getBody() {
        return Flux.range(1, 5)
                .map(i -> "body-" + i)
                .delayElements(Duration.ofMillis(100));
    }

    private static Flux<String> getEngine() {
        return Flux.range(1, 3)
                .map(i -> "engine-" + i)
                .delayElements(Duration.ofMillis(200));
    }

    private static Flux<String> getTires() {
        return Flux.range(1, 10)
                .map(i -> "tires-" + i)
                .delayElements(Duration.ofMillis(75));
    }

    private static Flux<Integer> getPassengers() {
        return Flux.range(1, 10);
    }
}
