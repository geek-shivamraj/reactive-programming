package com.reactive.programming;

import com.reactive.programming.pubsub.subscriber.SubscriberImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

/**
 *  FluxSink Default behavior
 *      - By default, FluxSink buffers/produces/generate all the items upfront to the Queue then subscriber
 *          can consume the items whenever it wants.
 *      - We might think this is bad, but it always depends on the use case/business requirements. In most of the cases,
 *          Publisher should be lazy as much as possible however in some cases, doing all work upfront can also be efficient.
 *
 *  - From Subscriber's point of view, it will simply request as usual, it does not know all these FluxSink internals.
 *
 *  Question: How many items can FluxSink put inside the Queue? Won't it get Out of Memory error if its keeps on adding items to the queue?
 *  Ans: Here Queue is unbounded i.e., it can go to MAX Integer size & beyond that we will get Out of Memory error but
 *      the basic assumption here is the subscriber will start consuming at some point.
 *
 *  - Assuming the Subscriber is very slow & the Producer is very fast, then the Queue might fill. This we call as Backpressure.
 *  - Reactive Programming provides some tools to handle backpressure that we will learn later.
 *
 */
public class FluxSinkDefaultBehaviorDemo {

    private static final Logger log = LoggerFactory.getLogger(FluxSinkDefaultBehaviorDemo.class);

    public static void main(String[] args) {

       var subscriber = new SubscriberImpl();

        Flux.<String>create(fluxSink -> {
            for(int i = 0; i < 5; i++) {
                var name = Util.faker().name().firstName();
                log.info("Generated name: {}", name);
                fluxSink.next(name);
            }
            fluxSink.complete();
        }).subscribe(subscriber);

        Util.sleepSeconds(2);
        subscriber.getSubscription().request(2);
        Util.sleepSeconds(2);
        subscriber.getSubscription().request(2);
        Util.sleepSeconds(2);
        subscriber.getSubscription().cancel();

        // Post cancel, subscriber can't request more items
        subscriber.getSubscription().request(2);
    }
}
