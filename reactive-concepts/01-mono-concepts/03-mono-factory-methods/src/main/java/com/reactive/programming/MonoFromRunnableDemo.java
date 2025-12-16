package com.reactive.programming;

import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 *  - Use Runnable when you want to run a task that doesn’t return a value. It can throw unchecked exceptions (like RuntimeException),
 *      but it cannot declare checked exceptions in its run() method signature.
 *
 *  - Use Mono.fromRunnable when you want to wrap a Runnable in a Mono. The resulting Mono<Void> will emit only onComplete if
 *      the task finishes successfully, or onError if the Runnable throws an exception.
 *
 *  - For side‑effects that don’t produce a result (logging, updating a counter, sending a notification)
 *
 *  - The behavior of Mono.fromRunnable() is deferred until subscription — exactly like fromSupplier or fromCallable. The difference is that
 *      Runnable doesn’t produce a value, so the resulting Mono is of type Mono<Void> and only signals completion or error.
 *
 */
public class MonoFromRunnableDemo {

    private static final Logger log = LoggerFactory.getLogger(MonoFromRunnableDemo.class);

    public static void main(String[] args) {
        getProductName(2).subscribe(DefaultSubscriber.create("sub1"));
    }

    private static Mono<String> getProductName(int productId) {
        if (productId == 1) {
            return Mono.fromSupplier(() -> Util.faker().commerce().productName());
        }
        // return Mono.empty();
        return Mono.fromRunnable(() -> notifyBusiness(productId));
    }

    private static void notifyBusiness(int productId) {
        log.info("Notifying business about the product {}", productId);
    }
}
