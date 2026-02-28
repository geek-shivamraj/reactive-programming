package com.reactive.programming;

import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.util.context.Context;

/**
 *  Scenario:
 *      - We've one book service (paid) as external service & we'll be sending request to get the book name.
 *      - Our app has 2 different types of users: Standard User & Premium User.
 *      - Problem: Our users are bombarding us with requests & we've to pay to book service for each request.
 *      - Solution: To limit the outbound call saying if
 *          - Standard User allow 2 calls / 5 seconds
 *          - Premium User allow 3 calls / 5 seconds
 *          - We will keep 1 user service that will limit the request to client based on user type.
 */
public class RateLimiterDemo {

    private static final Logger log = LoggerFactory.getLogger(RateLimiterDemo.class);

    public static void main(String[] args) {

        var client = new ExternalServiceClient();

        for (int i = 1; i <= 20; i++) {
            var j = i;
            client.getBook()
                    .doOnSuccess(s -> log.info("Request - {}, Response received - {}", j, s))
                    .doOnSubscribe(sub -> log.info("Request - {}", j))
                    //.contextWrite(Context.of("user", "sam"))          // standard user - 2 calls / 5 seconds
                    .contextWrite(Context.of("user", "mike"))         // prime user - 3 calls / 5 seconds
                    //.contextWrite(Context.of("user", "jake"))           // unknown user - 0 calls / 5 seconds
                    .subscribe(DefaultSubscriber.create("sub1"));

            Util.sleepSeconds(1);
        }

        Util.sleepSeconds(20);
    }
}
