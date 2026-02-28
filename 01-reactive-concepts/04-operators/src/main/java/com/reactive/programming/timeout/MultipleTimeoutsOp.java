package com.reactive.programming.timeout;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * - In case we have multiple timeouts then the timeout closest to the subscriber will take effect.
 *
 */
public class MultipleTimeoutsOp {

    private static final Logger log = LoggerFactory.getLogger(MultipleTimeoutsOp.class);

    public static void main(String[] args) {

        var mono = remoteService().timeout(Duration.ofSeconds(1), fallbackRemoteService());

        // We can reduce the timeout from above reusable mono (don't need to make any change to above mono)
        mono.timeout(Duration.ofMillis(800)).subscribe(DefaultSubscriber.create("sub1"));

        // This will not do anything. We can't increase the timeout than already in above reusable flux.
        mono.timeout(Duration.ofSeconds(3)).subscribe(DefaultSubscriber.create("sub2"));

        Util.sleepSeconds(5);
    }

    private static Mono<String> remoteService() {
        return Mono.fromSupplier(() -> "[service] " + Util.faker().commerce().productName()).delayElement(Duration.ofSeconds(2));
    }


    private static Mono<String> fallbackRemoteService() {
        return Mono.fromSupplier(() -> "[fallback service] " + Util.faker().commerce().productName())
                .delayElement(Duration.ofMillis(300)).doFirst(() -> System.out.println("Fallback service do-First!!"));
    }
}
