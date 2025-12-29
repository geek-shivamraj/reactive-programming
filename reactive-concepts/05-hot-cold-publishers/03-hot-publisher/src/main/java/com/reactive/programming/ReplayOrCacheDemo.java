package com.reactive.programming;


import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 *  - When we're using publish.autoConnect(0), the publisher will start producing items even if there is no subscriber to it & subscribers subscribing to it can't see
 *      the past items emitted.
 *      - But in some cases, the subscriber needs to know the past emitted items.
 *      - For e.g., stock price, if a subscriber joins, he needs to know the current stock price at that moment or may be a history of stock prices.
 *
 *      - For this requirement, Reactor provides replay() operator to cache the past emitted items (to be shared among late subscribers).
 *      - Using replay(), we can define how many past items to be replayed to the new subscriber. For e.g., replay(1) will replay only the last emitted item.
 *      - By default, replay() operator will replay all the past items emitted by the publisher (Long.MAX_VALUE).
 */
public class ReplayOrCacheDemo {

    private static final Logger log = LoggerFactory.getLogger(ReplayOrCacheDemo.class);

    public static void main(String[] args) {

        //var stockStream = stockStream().publish().autoConnect(0);
        var stockStream = stockStream().replay(1).autoConnect(0);

        Util.sleepSeconds(4);

        log.info("**************** sub1 - sam joining ****************");
        stockStream.subscribe(DefaultSubscriber.create("sub1 - sam"));

        Util.sleepSeconds(6);

        log.info("**************** sub2 - mike joining ****************");
        stockStream.subscribe(DefaultSubscriber.create("sub2 - mike"));

        Util.sleepSeconds(15);
    }

    private static Flux<Integer> stockStream() {
        return Flux.generate(sink -> sink.next(Util.faker().random().nextInt(10, 100)))
                .take(15)
                .delayElements(Duration.ofSeconds(3))
                .doOnNext(stock -> log.info("Emitting stock price is {}", stock))
                .cast(Integer.class);
    }
}
