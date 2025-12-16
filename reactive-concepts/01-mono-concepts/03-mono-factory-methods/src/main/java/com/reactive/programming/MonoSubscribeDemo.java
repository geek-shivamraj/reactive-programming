package com.reactive.programming;

import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 *  Mono.subcribe()
 *      Question 1: Where is the subscriber created ?
 *          - The subscriber is not something you explicitly create. Instead, the call to mono.subscribe(...) internally creates a Subscriber
 *              implementation for you.
 *          - When you call subscribe(...), Reactor builds a default LambdaSubscriber (a convenience subscriber that wraps your lambda functions
 *              for onNext, onError, and onComplete).
 *          - In your case, the lambda item -> log.info("Item: {}", item) is passed as the onNext consumer. Reactor wires this into the LambdaSubscriber.
 *          - That LambdaSubscriber is then subscribed to the Mono.just(1) publisher.
 *          - So the subscriber is implicitly created by Reactor — you don’t see it directly, but it’s there under the hood as a LambdaSubscriber.
 *
 *      Question 2: Where is onComplete() called ?
 *          - Reactor internally creates a LambdaSubscriber with your onNext lambda, a default onError handler, and a default onComplete that does nothing.
 *          - Since Mono.just(1) emits one item and then completes, the onComplete callback is invoked — but because you didn’t provide one,
 *              it’s just the default empty action.
 *          - If you want to see or handle completion explicitly, you can use overloaded subscribe() method to pass more arguments
 *
 *      Question 3: We didn't request any item but still we received the item. Why ?
 *          - Reactor’s default LambdaSubscriber (the one created when you use subscribe(...) with lambdas) automatically requests
 *              an unbounded number of items (Long.MAX_VALUE) from the publisher as soon as it subscribes.
 *          - This is a convenience feature: you don’t have to manually manage backpressure when using the simple subscribe(...) API.
 *          - That’s why you see the item even though you didn’t explicitly request it — the subscriber did it for you.
 *
 */
public class MonoSubscribeDemo {

    private static final Logger log = LoggerFactory.getLogger(MonoSubscribeDemo.class);

    public static void main(String[] args) {

        var mono = Mono.just(1);

        mono.subscribe(item -> log.info("Item: {}", item));

        // Case 1: We can use our Default Subscriber
        mono.subscribe(DefaultSubscriber.create("sub1"));
        mono.subscribe(DefaultSubscriber.create("sub2"));

        log.info("----------------------------------------------------------------------------");
        // Case 2: We can add onError & onComplete as below
        mono.subscribe(
                item -> log.info("Received Case 2 item: {}", item),
                error -> log.error("Error: ", error),
                () -> log.info("Case 2 Completed")
        );

        log.info("----------------------------------------------------------------------------");
        // Case 3: We can add onError, onComplete & also using our custom Subscription object to request or cancel.
        mono.subscribe(
                item -> log.info("Received Case 3 item: {}", item),
                error -> log.error("Error: ", error),
                () -> log.info("Case 3 Completed"),
                Subscription::cancel    // We're cancelling using subscription object so we won't receive any item.
        );

        log.info("----------------------------------------------------------------------------");
        // Case 4: Exception scenario
        mono = mono.map(i -> i / 0);
        mono.subscribe(
                item -> log.info("Received Case 4 item: {}", item),
                error -> log.error("Case 4 Error: ", error),
                () -> log.info("Case 4 Completed")
        );
    }
}
