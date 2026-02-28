package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  Use case
 *      - Suppose we've a ProductService & we're running multiple instances of ProductService behind load balancer.
 *      - let's image 1 instance is under load & it's not able to process the request.
 *      - In that case, we can use retry operator to retry the request so that load balancer can route the request to another instance.
 *
 *  Case when final retry is not successful
 *      - If final retry is not successful, then onError signal will be emitted with RetryExhaustedException: Retries exhausted: 2/2
 *          with original exception.
 *      - If we don't want subscriber to see the retrace exhausted exception, we can use option3 to just emit the original exception.
 */
public class RetryOperator {

    private static final Logger log = LoggerFactory.getLogger(RetryOperator.class);

    public static void main(String[] args) {

        //retryOption1();
        //retryOption2();
        retryOption3();

        Util.sleepSeconds(20);
    }

    private static void retryOption1() {
        getCountryNames()
                //.retry()                  // infinite retry
                //.retry(1)                 // retry only once
                .retry(2)        // retry 2 times
                .subscribe(DefaultSubscriber.create("sub1"));
    }

    /**
     *  - Prefer retryWhen over retry(2) as it provides more flexibility
     *      - Adding delay between retries
     *      - Adding callbacks
     *      - Retry signal details
     *      - Retry based on exception type using filter
     */
    private static void retryOption2() {
        getCountryNames()
                //.retryWhen(Retry.indefinitely())
                //.retryWhen(Retry.max(2))
                //.retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(2)))
                //.retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(2)).doBeforeRetry(rs -> log.info("Retrying... Error: {}", rs)))
                .retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(2))
                        .doBeforeRetry(rs -> log.info("Retrying... Error: {}", rs))
                        .filter(ex -> RuntimeException.class.equals(ex.getClass())))
                .subscribe(DefaultSubscriber.create("sub2"));
    }

    /**
     *  - If final retry is not successful, then onError signal will be emitted with original exception.
     */
    private static void retryOption3() {
        getCountryNames()
                .retryWhen(Retry.fixedDelay(1, Duration.ofSeconds(2))
                        .doBeforeRetry(rs -> log.info("Retrying... Error: {}", rs))
                        .filter(ex -> RuntimeException.class.equals(ex.getClass()))
                        .onRetryExhaustedThrow((spec, signal) -> signal.failure()))
                .subscribe(DefaultSubscriber.create("sub3"));
    }

    private static Mono<String> getCountryNames() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        return Mono.fromSupplier(() -> {
                    // This will throw error in first 2 iterations.
                    if (atomicInteger.incrementAndGet() < 3) {
                        throw new RuntimeException("oopss error!!");
                    }
                    return Util.faker().country().name();
                })
                .doOnError(error -> log.error("Error: {}", error.getMessage()))
                .doOnSubscribe(s -> log.info("Subscribing..."));
    }
}
