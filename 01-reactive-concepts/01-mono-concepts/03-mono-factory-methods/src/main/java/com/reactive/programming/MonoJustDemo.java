package com.reactive.programming;

import com.reactive.programming.subscriber.SubscriberImpl;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 *  Mono.just()
 *      - This is the easiest way to create a publisher.
 *      - This will be emitting when the subscriber subscribes to the publisher.
 *      - This mono just will accept any type.
 *      - There are many overloaded methods to subscribe.
 *      - Sometimes we'll have values (items to be emitted by publisher) already in the memory i.e., it doesn't have to come from DB
 *          or anywhere else, it's already in the application memory & for some reason we will have to create a publisher using that value
 *          then we can simply use "just" method to make it as a publisher.
 *      - For e.g., calling save(Publisher<T> publisher>) method, we've to send the arg as Publisher/Mono
 */
public class MonoJustDemo {

    private static final Logger log = LoggerFactory.getLogger(MonoJustDemo.class);

    public static void main(String[] args) {
        Publisher<String> mono = Mono.just("Hello");
        System.out.println("Mono: " + mono);

        // Subscribe to the publisher to emit items
        var subscriber = new SubscriberImpl();
        mono.subscribe(subscriber);

        // In reactive programming, we've to subscribe & request item
        subscriber.getSubscription().request(10);

        // Using Mono.just to create a publisher as save() method expects a publisher argument.
        save(Mono.just("Shivam"));
    }

    private static void save(Publisher<String> publisher) {
        var subscriber = new SubscriberImpl();
        publisher.subscribe(subscriber);
        // This will give the item to be persisted
        subscriber.getSubscription().request(1);
    }
}


