package com.reactive.programming;

import com.reactive.programming.client.ExternalServiceClient;
import com.reactive.programming.helper.DefaultSubscriber;
import com.reactive.programming.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockingEventLoopDemo {

    private static final Logger log = LoggerFactory.getLogger(BlockingEventLoopDemo.class);

    public static void main(String[] args) {

        var client = new ExternalServiceClient();

        log.info("starting...");
        for (int i = 0; i < 10; i++) {
            client.getProductNames(i)
                    .map(BlockingEventLoopDemo::process)
                    .subscribe(DefaultSubscriber.create("sub1"));
        }

        Util.sleepSeconds(15);
    }

    private static String process(String input) {
        Util.sleepSeconds(1);
        return input + "-processed";
    }
}
