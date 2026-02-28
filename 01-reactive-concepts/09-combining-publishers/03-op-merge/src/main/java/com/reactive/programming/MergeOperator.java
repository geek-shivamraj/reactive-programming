package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 *  Merge operator
 *      - Instead of subscriber subscribing to each publisher one by one, we can use merge() operator to merge these into one single publisher
 *          & then the subscriber can subscribe to this merged publisher so we will be subscribing all the producers at the same time.
 *      - In which order, the subscribers will receive the items is not guaranteed (unlike in case of concatWith() or startWith()).
 *          i.e., whichever publisher will emit item first, the subscriber will receive that item first.
 *
 *      - Also in case we cancel the subscription, all the publishers subscriptions will be cancelled at the same time.
 *
 *      - We've 2 methods
 *          - static <I> Flux<I> merge(Publisher<? extends I>... sources)
 *          - Flux<T> mergeWith(Publisher<? extends T> other)
 *
 */
public class MergeOperator {

    public static void main(String[] args) {

        //merge_factory_method();

        mergeWith_publisher();

        // Blocking main thread to let other threads finish their execution
        Util.sleepSeconds(3);

    }

    // static <I> Flux<I> merge(Publisher<? extends I>... sources)
    private static void merge_factory_method() {
        Flux.merge(producer1(), producer2(), producer3())
                //.take(2)      // to see cancelling case
                .subscribe(DefaultSubscriber.create("sub1"));
    }

    // Flux<T> mergeWith(Publisher<? extends T> other)
    private static void mergeWith_publisher() {
        producer1()
                .mergeWith(producer2())
                .mergeWith(producer3())
                //.take(2)      // to see cancelling case
                .subscribe(DefaultSubscriber.create("sub1"));
    }

    private static Flux<Integer> producer1() {
        return Flux.just(1, 2, 3)
                .transform(Util.fluxLogger("producer1"))
                .delayElements(Duration.ofMillis(10));
    }

    private static Flux<Integer> producer2() {
        return Flux.just(51, 52, 53)
                .transform(Util.fluxLogger("producer2"))
                .delayElements(Duration.ofMillis(10));
    }

    private static Flux<Integer> producer3() {
        return Flux.just(11, 12, 13)
                .transform(Util.fluxLogger("producer3"))
                .delayElements(Duration.ofMillis(10));
    }

    private static Flux<Integer> producer4() {
        return Flux.error(new RuntimeException("oopss error"));
    }
}
