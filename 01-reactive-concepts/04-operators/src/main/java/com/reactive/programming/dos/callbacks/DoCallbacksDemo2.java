package com.reactive.programming.dos.callbacks;

import com.reactive.programming.helper.DefaultSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

/**
 *      Note:
 *          - Except doOnNext, all other methods will not mutate the value & doOnNext method will be executed for each item.
 *          - doFirst() will be executed before anything, even before subscription from Subscriber to Publisher.
 *          - doOnSubscribe() will be invoked & subscription object will be passed from Publisher to Subscriber.
 *          - doOnRequest() will be invoked when request is made from Subscriber to Publisher.
 *              If no take()/loop is defined to limit the number of items to be requested,
 *              then by default request(Long.MAX_VALUE) - 9223372036854775807 will be made.
 *
 *          - doOnNext() will be executed for each requested item from Publisher to Subscriber & all below methods will be
 *              executed based on the signal received.
     *          - doOnComplete() will be executed when sequence is completed from Publisher to Subscriber.
     *          - doOnError() will be invoked when Producer sends error signal from Publisher to Subscriber.
     *          - doOnTerminate() will be invoked when sequence is terminated either by complete or error from Publisher to Subscriber.
     *          - doOnCancel() will be invoked when Consumer cancels the subscription from Subscriber to Publisher.
     *          - doFinally() will be invoked finally irrespective of the reason from Publisher to Subscriber.
     *          - doOnDiscard() will be invoked when Consumer discards the item from the Publisher.
 *
 *
 *      - The execution order is very important. The code execution will be from Subscriber point of view (not publisher)
 *          i.e., when subscriber subscribes, then everything starts. That's why doFirst-2 will be executed before doFirst-1
 *
 *      - Lifecycle Flow:
 *          - doFirst --> doOnSubscribe --> doOnRequest --> doOnError --> doOnTerminate --> doFinally
 */
public class DoCallbacksDemo2 {

    private static final Logger log = LoggerFactory.getLogger(DoCallbacksDemo2.class);

    public static void main(String[] args) {

        Flux.create(fluxSink -> {
                    log.info("Producer begins...");
//                    for (int i = 0; i < 4; i++) {
//                        fluxSink.next(i);
//                    }
                    // fluxSink.complete();
                    fluxSink.error(new RuntimeException("oops"));
                    log.info("Producer ends!!");
                })
                .doOnComplete(() -> log.info("doOnComplete-1"))
                .doFirst(() -> log.info("doFirst-1"))
                .doOnNext(item -> log.info("doOnNext-1: {}", item))
                .doOnSubscribe(subscription -> log.info("doOnSubscribe-1: {}", subscription))
                .doOnRequest(request -> log.info("doOnRequest-1: {}", request))
                .doOnError(error -> log.info("doOnError-1: {}", error.getMessage()))
                .doOnTerminate(() -> log.info("doOnTerminate-1"))   // complete or error case
                .doOnCancel(() -> log.info("doOnCancel-1"))
                .doOnDiscard(Object.class, o -> log.info("doOnDiscard-1: {}", o))
                .doFinally(signal -> log.info("doFinally-1: {}", signal)) // finally irrespective of the reason

                //.take(2)

                .doOnComplete(() -> log.info("doOnComplete-2"))
                .doFirst(() -> log.info("doFirst-2"))
                .doOnNext(item -> log.info("doOnNext-2: {}", item))
                .doOnSubscribe(subscription -> log.info("doOnSubscribe-2: {}", subscription))
                .doOnRequest(request -> log.info("doOnRequest-2: {}", request))
                .doOnError(error -> log.info("doOnError-2: {}", error.getMessage()))
                .doOnTerminate(() -> log.info("doOnTerminate-2"))   // complete or error case
                .doOnCancel(() -> log.info("doOnCancel-2"))
                .doOnDiscard(Object.class, o -> log.info("doOnDiscard-2: {}", o))
                .doFinally(signal -> log.info("doFinally-2: {}", signal)) // finally irrespective of the reason

                //.take(4)

                .subscribe(DefaultSubscriber.create("sub1"));
    }
}
