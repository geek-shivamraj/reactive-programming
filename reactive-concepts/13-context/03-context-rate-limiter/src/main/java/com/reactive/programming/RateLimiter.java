package com.reactive.programming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *  - Below, we implemented a basic RateLimiter where the categoryAttempts map is mutated to control access.
 *      However, this approach is not thread-safe as-is.
 *  - For a simple & effective fix, we can make the canAllow method synchronized to ensure atomic access. This ensures
 *      that concurrent calls don't interfere with each other when checking & updating the attempt count.
 *
 */
public class RateLimiter {

    private static final Logger log = LoggerFactory.getLogger(RateLimiter.class);
    private static final Map<String, Integer> categoryAttempts = Collections.synchronizedMap(new HashMap<>());

    static {
        refresh();
    }

    static <T> Mono<T> limitCalls() {
        return Mono.deferContextual(ctx -> {
            var allowCall = ctx.<String>getOrEmpty("category")
                    .map(RateLimiter::canAllow)
                    .orElse(false);

            return allowCall ? Mono.empty() : Mono.error(new RuntimeException("Exceeded the given limit"));
        });
    }

    private static synchronized boolean canAllow(String category) {
        var attempts = categoryAttempts.getOrDefault(category, 0);
        log.info("********* Attempts: {} *********", attempts);
        if(attempts > 0) {
            categoryAttempts.put(category, attempts - 1);
            return true;
        }
        return false;
    }

    public static void refresh() {
        Flux.interval(Duration.ofSeconds(5))
                .startWith(0L)          // To handle the start case where the categoryAttempts map is initialized
                .subscribe(i -> {
                    categoryAttempts.put("standard", 2);
                    categoryAttempts.put("prime", 3);
                });
    }
}
