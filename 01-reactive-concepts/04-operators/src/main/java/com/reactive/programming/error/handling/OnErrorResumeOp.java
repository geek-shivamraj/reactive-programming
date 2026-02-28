package com.reactive.programming.error.handling;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *  - Imp. Note:
 *      - Position of an operator is very important to clearly use the functionality as accordingly the operator's publisher
 *          & subscriber would vary.
 *
 *  - onErrorResume
 *      - onErrorReturn is helpful for hard-coded fallback value but if we want to call a backup service then onErrorResume is helpful.
 *      - Also, based on exception type, we can have fallbacks as well.
 *
 */
public class OnErrorResumeOp {

    private static final Logger log = LoggerFactory.getLogger(OnErrorResumeOp.class);

    public static void main(String[] args) {

        Flux.range(1, 10)
                .map(i -> i == 5 ? 5/0 : i) // intentional
                .onErrorResume(ArithmeticException.class, ex -> backupFallbackService1())
                .onErrorResume(ex -> backupFallbackService2()) // Generic exception fallback
                .onErrorReturn(-5) // In case, fallback fails.
                .subscribe(DefaultSubscriber.create());

        System.out.println("----------------------------------------------------------------------------------");

        Flux.error(new RuntimeException("Oops!"))
                .onErrorResume(ArithmeticException.class, ex -> backupFallbackService1())
                .onErrorResume(ex -> backupFallbackService2()) // Generic exception fallback
                .onErrorReturn(-5)
                //.onErrorResume(ex -> backupFallbackService()) // Generic exception fallback
                .subscribe(DefaultSubscriber.create());
    }

    private static Mono<Integer> backupFallbackService1() {
        return Mono.fromSupplier(() -> {
            log.info("Calling backup service 1 ...");
            return Util.faker().random().nextInt(10, 100);
        });
    }

    private static Mono<Integer> backupFallbackService2() {
        return Mono.fromSupplier(() -> {
            log.info("Calling backup service 2...");
            return Util.faker().random().nextInt(100, 1000);
        });
    }

    private static void hardCodedFallback() {
        // If we don't want to receive the exception, rather provide a hardcoded fallback value using onErrorReturn
        Flux.range(1, 10)
                .map(i -> i == 5 ? 5/0 : i) // intentional
                .onErrorReturn(ArithmeticException.class, -1)
                .onErrorReturn(IllegalArgumentException.class, -2)
                .onErrorReturn(-3)  // Generic fallback value
                .subscribe(DefaultSubscriber.create());
    }
}
