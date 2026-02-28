package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

/**
 *  - The startWith operator in Project Reactor allows you to prepend one or more values to a reactive sequence before it begins emitting its normal items.
 *  - In other words, it ensures that subscribers see specified initial values first, followed by the actual emissions from the source Flux or Mono.
 */
public class StartWithOperator {

    private static final Logger log = LoggerFactory.getLogger(StartWithOperator.class);

    public static void main(String[] args) {

        //startWith_use_case1();

        //startWith_use_case2();

        //startWith_use_case3();

        startWith_use_case4();

        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(3);

    }

    // Multiple startWith (Execution order would be downstream to upstream)
    // i.e, 1000 -> producer2() -> producer1()
    private static void startWith_use_case4() {
        producer1()
                .startWith(producer2())
                .startWith(1000)
                .subscribe(DefaultSubscriber.create("sub1"));
    }

    // Flux<T> startWith(Publisher<? extends T> publisher)
    private static void startWith_use_case3() {
        producer1()
                .startWith(producer2())
                .subscribe(DefaultSubscriber.create("sub1"));
    }

    // Flux<T> startWith(Iterable<? extends T> iterable)
    private static void startWith_use_case2() {
        producer1()
                .startWith(List.of(-2, -1, 0))
                .subscribe(DefaultSubscriber.create("sub1"));
    }

    // Flux<T> startWith(T... values)
    private static void startWith_use_case1() {
        producer1()
                .startWith(-1, 0)
                //.take(2)    // We already got 2 values (-1, 0) so producer1 is even subscribed.
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
