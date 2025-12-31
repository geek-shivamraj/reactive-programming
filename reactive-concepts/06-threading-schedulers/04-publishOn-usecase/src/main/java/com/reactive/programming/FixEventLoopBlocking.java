package com.reactive.programming;

import com.reactive.programming.client.UnblockingServiceClient;
import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  - By adding publishOn(Schedulers.boundedElastic()) to the external client, we are telling Reactor to use a different thread to call the API
 *      & different thread to process the response.
 *      - This will fix the EventLoopBlocking issue.
 *
 */
public class FixEventLoopBlocking {

    private static final Logger log = LoggerFactory.getLogger(FixEventLoopBlocking.class);

    public static void main(String[] args) {

        //var client = new ExternalServiceClient();
        var client = new UnblockingServiceClient();

        log.info("starting...");
        for (int i = 0; i < 10; i++) {
            client.getProductNames(i)
                    .map(FixEventLoopBlocking::process)
                    .subscribe(DefaultSubscriber.create("sub1"));
        }

        Util.sleepSeconds(5);
    }

    private static String process(String input) {
        Util.sleepSeconds(1);
        return input + "-processed";
    }
}
