package com.reactive.programming.timeout;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 *  - These Mono/Flux, they all represent the data that might be coming from Remote Service / Database & often times when we make a n/w call we
 *      might not get the response in a specific time.
 *  - For e.g., If you receive a request then you call DB / remote service, they might respond very slow, or they might not respond at all.
 *      In these cases, We don't want to wait indefinitely. We want to have a timeout.
 *  - timeout operator is helpful to handle these cases.
 *  - We can use onErrorResume to call another service or it can be done as part of timeout handler only.
 *  - doFirst will only be printed when fallbackRemoteService is subscribed else not.
 *
 */
public class TimeoutOp {

    public static void main(String[] args) {
        remoteService()
                .timeout(Duration.ofSeconds(2), fallbackRemoteService())
                //.onErrorReturn("fallback")
                .subscribe(DefaultSubscriber.create("sub1"));

        Util.sleepSeconds(5);
    }

    private static Mono<String> remoteService() {
        return Mono.fromSupplier(() -> "[service] " + Util.faker().commerce().productName())
                .delayElement(Duration.ofSeconds(3));
    }


    private static Mono<String> fallbackRemoteService() {
        return Mono.fromSupplier(() -> "[fallback service] " + Util.faker().commerce().productName())
                .delayElement(Duration.ofMillis(300))
                .doFirst(() -> System.out.println("Fallback service do-First!!"));
    }
}
