package com.reactive.programming.delay;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 *  - Right now Flux.range() will emit all the items from 1 to 10 to subscriber. We can delay the rate at which items are emitted to the subscriber
 *      by using delayElements operator.
 *  - It will be done by a separate thread. So, we've to block the main thread here.
 *
 *  - Note:
 *      - delayElements() will not internally hold the items in memory rather it keeps on requesting from the Publisher.
 *
 */
public class DelayOperator {

    public static void main(String[] args) {

        Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1))
                .subscribe(DefaultSubscriber.create("sub1"));

        Util.sleepSeconds(12);
    }
}
