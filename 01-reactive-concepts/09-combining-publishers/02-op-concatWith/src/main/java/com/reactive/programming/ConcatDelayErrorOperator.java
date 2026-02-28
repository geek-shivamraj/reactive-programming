package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 *  - In case of concatWith(), the 1st/2nd publisher might emit an error signal in that case
 *      by default, subsequent publishers will not be emitting items.
 *
 *  - To continue with the subsequent publisher subscriptions & delay the error signal, we can use
 *      Flux.concatDelayError(producer1(), producer2(), producer3()) operator.
 *
 */
public class ConcatDelayErrorOperator {

    private static final Logger log = LoggerFactory.getLogger(ConcatDelayErrorOperator.class);

    public static void main(String[] args) {

        //concatWith_publisher();

        concatDelayError();

        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(3);

    }

    // Flux<T> concatWith(Publisher<? extends T> other)
    private static void concatWith_publisher() {
        producer1()
                .concatWith(producer3())
                .concatWith(producer2())
                .subscribe(DefaultSubscriber.create("sub1"));
    }

    // static <T> Flux<T> concatDelayError(Publisher<? extends T>... sources)
    private static void concatDelayError() {
        Flux.concatDelayError(producer1(), producer3(), producer2())
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

    private static Flux<Integer> producer3() {
        return Flux.error(new RuntimeException("oopss error!!"));
    }
}
