package com.reactive.programming;

import com.reactive.programming.pubsub.subscriber.SubscriberImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

/**
 *  FluxSink Custom behavior - Produce on Demand
 *     - So, if we want to produce items early, we can rely on FluxSink default behavior but if we want to produce items
 *          only when the subscriber requests for it, we can use "fluxSink.onRequest()" method.
 *     - fluxSink.onRequest() method is a callback method & it will be invoked when the subscriber requests for items.
 *
 */
public class FluxSinkCustomBehaviorDemo {

    private static final Logger log = LoggerFactory.getLogger(FluxSinkCustomBehaviorDemo.class);

    public static void main(String[] args) {

       var subscriber = new SubscriberImpl();

        Flux.<String>create(fluxSink -> {

            fluxSink.onRequest(request -> {
                for(int i = 0; i < request && !fluxSink.isCancelled(); i++) {
                    var name = Util.faker().name().firstName();
                    log.info("Generated: {}", name);
                    fluxSink.next(name);
                }
            });
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
