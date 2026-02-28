package com.reactive.programming;

import com.reactive.programming.client.ExternalServiceClient;
import com.reactive.programming.client.exceptions.ServerError;
import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class RepeatExternalServices {

    private static final Logger log = LoggerFactory.getLogger(RepeatExternalServices.class);

    public static void main(String[] args) {

        //repeat();

        retry();

        // This will block main thread for 20 seconds.
        Util.sleepSeconds(20);
    }

    private static void repeat() {
        var client = new ExternalServiceClient();
        client.getCountry()
                .repeat()
                .takeUntil(c -> c.equalsIgnoreCase("canada"))
                .subscribe(DefaultSubscriber.create("sub1"));
    }

    private static void retry() {
        var client = new ExternalServiceClient();
        client
                //.getProductName(1)                // ClientError
                .getProductName(2)        // ServerError
                .retryWhen(retryOnServerError())
                .subscribe(DefaultSubscriber.create("sub2"));
    }

    private static Retry retryOnServerError() {
        return Retry.fixedDelay(20, Duration.ofSeconds(1))
                .filter(ex -> ServerError.class.equals(ex.getClass()))
                .doBeforeRetry(rs -> log.info("Retrying {}", rs.failure().getMessage()));
    }
}
