package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

/**
 *  - concatWith is opposite of startWith.
 *  - Using concatWith, the first publisher emits all its elements & only after 1st publisher completes, 2nd publisher emits its elements.
 *  - Guarantees ordered, sequential emission - no interleaving.
 *  - We've 2 concatWith() operators & 1 factory method
 *      - concatWithValues(T... values)
 *      - concatWith(Publisher<? extends T> publisher)
 *      - Flux.concat(Publisher<? extends T>... sources)
 *
 */
public class ConcatWithOperator {

    private static final Logger log = LoggerFactory.getLogger(ConcatWithOperator.class);

    public static void main(String[] args) {

        //concatWith_values();

        //concatWith_publisher();

        concat_factory_method();

        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(3);

    }

    // static <T> Flux<T> concat(Publisher<? extends T>... sources)
    private static void concat_factory_method() {
        Flux.concat(producer1(), producer2())
                .subscribe(DefaultSubscriber.create("sub1"));
    }

    // Flux<T> concatWith(Publisher<? extends T> other)
    private static void concatWith_publisher() {
        producer1()
                .concatWith(producer2())
                //.take(2)          // In this case, we won't be disturbing producer2
                .subscribe(DefaultSubscriber.create("sub1"));
    }

    // Flux<T> concatWithValues(T... values)
    private static void concatWith_values() {
        producer1()
                .concatWithValues(-1, 0)
                .subscribe(DefaultSubscriber.create("sub1"));
    }

    private static Flux<Integer> producer1() {
        return Flux.just(1, 2, 3)
                .doOnSubscribe(s -> log.info("subscribing to producer1"))
                .delayElements(Duration.ofMillis(10));
    }

    private static Flux<Integer> producer2() {
        return Flux.just(51, 52, 53)
                .doOnSubscribe(s -> log.info("subscribing to producer2"))
                .delayElements(Duration.ofMillis(10));
    }
}
