package com.reactive.programming;

import com.reactive.programming.client.ExternalServiceClient;
import org.apache.commons.lang3.stream.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *  - This is a non-blocking IO, main thread exists immediately. Hence, we will block the main thread for 6 seconds.
 *
 *  - We can see how efficiently we can transfer the messages from one application to another application,
 *      how efficiently we can consume these messages using reactive programming.
 *
 */
public class NonBlockingIODemo {

    public static final Logger log = LoggerFactory.getLogger(NonBlockingIODemo.class);

    public static void main(String[] args) {
        var client = new ExternalServiceClient();

        log.info("Calling External Service....");
        client.getNames()
                .subscribe(DefaultSubscriber.create("sub1"));

        client.getNames()
                .subscribe(DefaultSubscriber.create("sub2"));

        Util.sleepSeconds(6);
    }
}
